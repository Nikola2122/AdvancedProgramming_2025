package LABS.LAB3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO: Add classes and implement methods

class Applicant implements Comparable<Applicant> {
    int id;
    String name;
    double gpa;
    List<SubjectWithGrade> subjects;
    StudyProgramme programme;
    double points;

    public Applicant(int id, String name, double gpa, List<SubjectWithGrade> subjects, StudyProgramme programme) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
        this.subjects = subjects;
        this.programme = programme;
    }

    void calculatePoints(List<String> subjects2){
        double sum = subjects.stream().mapToDouble(s -> {
            if(subjects2.contains(s.getSubject())){
                return s.getGrade() * 2;
            }
            else{
                return s.getGrade() * 1.2;
            }
        }).sum();
        points = gpa * 12 + sum;
    }

    void addSubjectAndGrade(String subject, int grade){
        subjects.add(new SubjectWithGrade(subject, grade));
    }

    @Override
    public int compareTo(Applicant o) {
        return Double.compare(o.points, this.points);
    }

    @Override
    public String toString() {
        return String.format("Id: %d, Name: %s, GPA: %.1f - %.2f", id, name, gpa, points);
    }

    public static List<SubjectWithGrade> grades(List<String> line){
        List<SubjectWithGrade> grades = IntStream.range(0, line.size()).mapToObj(i -> {
            if(i!= line.size()-1 && i % 2 == 0){
                String name = line.get(i);
                int grade = Integer.parseInt(line.get(i+1));
                return new SubjectWithGrade(name, grade);
            }
            else return null;
        }).collect(Collectors.toList());
        return grades.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}

class StudyProgramme{
    String id;
    String name;
    int numPublicQuota;
    int numPrivateQuota;
    int enrolledPublicQuota;
    int enrolledPrivateQuota;
    List<Applicant> applicants;
    List<Applicant> pub;
    List<Applicant> prv;
    List<Applicant> rej;
    Faculty faculty;

    public StudyProgramme(String id, String name, Faculty faculty, int numPublicQuota, int numPrivateQuota){
        this.id = id;
        this.name = name;
        this.faculty = faculty;
        this.numPublicQuota = numPublicQuota;
        this.numPrivateQuota = numPrivateQuota;
        this.applicants = new ArrayList<>();
    }

    void addApplicant(Applicant applicant){
        applicants.add(applicant);
    }

    void calculateEnrollmentNumbers(){
        List<Applicant> sorted = applicants.stream()
                .sorted()
                .collect(Collectors.toList());

        pub = sorted.stream()
                .limit(numPublicQuota)
                .collect(Collectors.toList());

        prv = sorted.stream()
                .skip(numPublicQuota)
                .limit(numPrivateQuota)
                .collect(Collectors.toList());

        rej = sorted.stream()
                .skip(numPublicQuota + numPrivateQuota)
                .collect(Collectors.toList());

        enrolledPublicQuota = pub.size();
        enrolledPrivateQuota = prv.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n");
        sb.append("Public Quota: \n");
        if(!pub.isEmpty()){
            sb.append(pub.stream().map(Applicant::toString).collect(Collectors.joining("\n"))).append("\n");
        }
        sb.append("Private Quota: \n");
        if(!prv.isEmpty()){
            sb.append(prv.stream().map(Applicant::toString).collect(Collectors.joining("\n"))).append("\n");
        }
        sb.append("Rejected: \n");
        if(!rej.isEmpty()){
            sb.append(rej.stream().map(Applicant::toString).collect(Collectors.joining("\n"))).append("\n");
        }
        return sb.toString();
    }
    double percent() {
        return ((double)(enrolledPublicQuota + enrolledPrivateQuota) /
                (numPublicQuota + numPrivateQuota)) * 100;
    }
}

class SubjectWithGrade
{
    private String subject;
    private int grade;
    public SubjectWithGrade(String subject, int grade) {
        this.subject = subject;
        this.grade = grade;
    }
    public String getSubject() {
        return subject;
    }
    public int getGrade() {
        return grade;
    }
}

class EnrollmentsIO {
    public static void printRanked(List<Faculty> faculties) {
        faculties.stream().sorted(Comparator.comparing(Faculty::getNumber)).forEach(System.out::println);
    }

    public static void readEnrollments(List<StudyProgramme> studyProgrammes, InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        reader.lines().forEach(l -> {
            String [] tokens = l.split(";");
            String id = tokens[0];
            String name = tokens[1];
            double gpa = Double.parseDouble(tokens[2]);
            List<SubjectWithGrade> grades = Applicant.grades(Arrays.asList(tokens).stream().skip(3).limit(tokens.length-1)
                    .collect(Collectors.toList()));
            String idOfSP = tokens[tokens.length-1];
            StudyProgramme programme = studyProgrammes.stream().filter(s -> s.id.equals(idOfSP)).findFirst().orElse(null);

            Applicant a = new Applicant(Integer.parseInt(id), name, gpa, grades, programme);
            a.calculatePoints(programme.faculty.getSubjects());
            programme.addApplicant(a);
        });
    }
}
class Faculty{
    String name;
    List<String> subjects;
    List<StudyProgramme> programs;

    public Faculty(String name) {
        this.name = name;
        this.subjects = new ArrayList<>();
        this.programs = new ArrayList<>();
    }

    void addSubject(String sub){
        subjects.add(sub);
    }

    void addStudyProgramme(StudyProgramme sp){
        programs.add(sp);
    }

    public String getName() {
        return name;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public List<StudyProgramme> getPrograms() {
        return programs;
    }

    public int getNumber(){
        return subjects.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Faculty: ").append(name).append("\n");
        sb.append("Subjects: ").append(subjects).append("\n");
        sb.append("Study Programmes: ").append("\n");
        sb.append(programs.stream().sorted(Comparator.comparing(StudyProgramme::percent).reversed()).
                map(StudyProgramme::toString).collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
public class EnrollmentsTest {

    public static void main(String[] args) {
        Faculty finki = new Faculty("FINKI");
        finki.addSubject("Mother Tongue");
        finki.addSubject("Mathematics");
        finki.addSubject("Informatics");

        Faculty feit = new Faculty("FEIT");
        feit.addSubject("Mother Tongue");
        feit.addSubject("Mathematics");
        feit.addSubject("Physics");
        feit.addSubject("Electronics");

        Faculty medFak = new Faculty("MEDFAK");
        medFak.addSubject("Mother Tongue");
        medFak.addSubject("English");
        medFak.addSubject("Mathematics");
        medFak.addSubject("Biology");
        medFak.addSubject("Chemistry");

        StudyProgramme si = new StudyProgramme("SI", "Software Engineering", finki, 4, 4);
        StudyProgramme it = new StudyProgramme("IT", "Information Technology", finki, 2, 2);
        finki.addStudyProgramme(si);
        finki.addStudyProgramme(it);

        StudyProgramme kti = new StudyProgramme("KTI", "Computer Technologies and Engineering", feit, 3, 3);
        StudyProgramme ees = new StudyProgramme("EES", "Electro-energetic Systems", feit, 2, 2);
        feit.addStudyProgramme(kti);
        feit.addStudyProgramme(ees);

        StudyProgramme om = new StudyProgramme("OM", "General Medicine", medFak, 6, 6);
        StudyProgramme nurs = new StudyProgramme("NURS", "Nursing", medFak, 2, 2);
        medFak.addStudyProgramme(om);
        medFak.addStudyProgramme(nurs);

        List<StudyProgramme> allProgrammes = new ArrayList<>();
        allProgrammes.add(si);
        allProgrammes.add(it);
        allProgrammes.add(kti);
        allProgrammes.add(ees);
        allProgrammes.add(om);
        allProgrammes.add(nurs);

        EnrollmentsIO.readEnrollments(allProgrammes, System.in);

        List<Faculty> allFaculties = new ArrayList<>();
        allFaculties.add(finki);
        allFaculties.add(feit);
        allFaculties.add(medFak);

        allProgrammes.stream().forEach(StudyProgramme::calculateEnrollmentNumbers);

        EnrollmentsIO.printRanked(allFaculties);

    }


}
