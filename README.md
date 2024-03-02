# Use case JPA, Hibernate Spring Data, One To Many, One To One | Gestion d'un hopital

Dans cette partie on va traiter  les relations One To Many et One To One avec l'utilisation de JPA, Hibernate et Spring web pour implémenter un Système de gestion hôpital. Ce projet va initialiser par H2 vers la fin nous merger vers MySql.
## Les dépandances :

- Lombok
- Spring web
- Spring Data JPA
- H2 Database

## Configuration de base de données

Dans le fichier ``` application.properties ``` :

``` properties
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:hospital
server.port=8080
spring.datasource.username=root
spring.datasource.password=
```

## Les entités


- ``` Patient.java ```
- ``` Consultation.java ```
- ``` Medecin.java ```
- ``` RendezVous.java ```
- ``` StatusRDV.java ``` : enumération permet de connaitre l'état de rendez vous et qui prendra 3 possibilités : PENDING / CANCELED / DONE
## Les repositories


- ``` PatientRepository.java ```
- ``` ConsultationRepository.java ```
- ``` MedecinRepository.java ```
- ``` RendezVousRepository.java ```

## Les services

- ``` HospitalService.java ```
- ``` HospitalServiceImpl.java ```

## Les controllers

- ``` PatientRestController.java ```

## Test

Dans la classe ```HopitalApplication.java``` :

``` java
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


```
Les données peuvent être vérifiées en accédant simplement à ce lien http://localhost:8080/h2-console

## Changer Base de données à MySQL

On va ajouter ces parametres au fichier application.properties.

``` properties
  server.port=8080
  spring.datasource.username=root
  spring.datasource.password=
  spring.datasource.url=jdbc:mysql://localhost:3306/hospital?createDatabaseIfNotExist=true
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
  spring.jpa.show-sql=true
```
Dans le fichier pom.xml , on ajoute :

``` xml
   <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.32</version>
   </dependency>
```

## Test

Pour confirmer que la migration de H2 vers MySQL s'est déroulée avec succès et que les données ont été correctement insérées dans la base de données, nous pouvons accéder à l'URL ```localhost:8080/patients```, grâce au code implémenté dans la classe ```PatientRestController.java```.







``` java
@RestController
public class PatientRestController {
    @Autowired
    private PatientRepository patientRepository;
    @GetMapping("/patients")
    public List<Patient> patientList(){
        return patientRepository.findAll();
    };
}
````


Ce projet nous a offert une opportunité d'approfondir notre compréhension de l'implémentation des relations One-to-Many et One-to-One en Java grâce à l'API JPA. Nous avons débuté notre exploration avec Hibernate et avons également étudié les méthodes de configuration d'un projet Spring pour simplifier le travail des développeurs.