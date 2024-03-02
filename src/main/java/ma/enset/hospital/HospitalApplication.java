package ma.enset.hospital;
import ma.enset.hospital.entities.*;
import ma.enset.hospital.repositories.ConsultationRepository;
import ma.enset.hospital.repositories.MedecinRepository;
import ma.enset.hospital.repositories.PatientRepository;
import ma.enset.hospital.repositories.RendezVousRepository;
import ma.enset.hospital.service.IHospitalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;
@SpringBootApplication
public class HospitalApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }
@Bean
    CommandLineRunner start(IHospitalService hospitalService,PatientRepository patientRepository,RendezVousRepository rendezVousRepository,MedecinRepository medecinRepository){
   return arqs -> {
       Stream.of("Mohamed","Saad","Aya")
               .forEach(name->{ Patient patient=new Patient();
                   patient.setNom(name);
                   patient.setDateNaissance(new Date());
                   patient.setMalade(false);
                   hospitalService.savePatient(patient);});
       Stream.of("yassmine","younes","ghali")
               .forEach(name->{Medecin medecin=new Medecin();
                   medecin.setNom(name);
                   medecin.setEmail(name+"@gmail.com");
                   medecin.setSpecialite(Math.random()>0.5?"Cardio":"Dentiste");
                   hospitalService.saveMedecin(medecin);});
       Patient patient=patientRepository.findById(1L).orElse(null);
       Patient patient1=patientRepository.findByNom("Mohamed");
       Medecin medecin=medecinRepository.findByNom("yassmine");
       RendezVous rendezVous=new RendezVous();
       rendezVous.setDate(new Date());
       rendezVous.setStatus(StatusRDV.PENDING);
       rendezVous.setMedecin(medecin);
       rendezVous.setPatient(patient);
       RendezVous saveDRDV = hospitalService.saveRDV(rendezVous);
       System.out.println(saveDRDV.getId());
       RendezVous rendezVous1=rendezVousRepository.findAll().get(0);
       Consultation consultation=new Consultation();
       consultation.setDateConsultation(new Date());
       consultation.setRendezVous(rendezVous1);
       consultation.setRapport("Rapport de la consultation .....");
      hospitalService.saveConsultation(consultation);
   };}}
