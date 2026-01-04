package LABS.LAB9;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface XMLComponent {
    void addAttribute(String name, String value);
    void addComponent(XMLComponent component);
    void print(int level);
}

abstract class XMLClass implements XMLComponent {
    String name;
    Map<String, String> attributes;

    public XMLClass(String name){
        attributes = new LinkedHashMap<>();
        this.name=name;
    }

    @Override
    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }

    public String getAttributes(){
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        attributes.forEach((k,v)->{
            builder.append(k).append("=").append("\"").append(v).append("\" ");
        });
        if(builder.length()>0){
            builder.deleteCharAt(builder.length()-1);
        }
        return builder.toString();
    }
}

class XMLLeaf extends XMLClass {

    String text;

    public XMLLeaf(String name, String text) {
        super(name);
        this.text=text;
    }


    @Override
    public void addComponent(XMLComponent component) {

    }

    @Override
    public void print(int level) {
        String r = "    ".repeat(level);
        String str = String.format("<%s%s>%s</%s>", name, getAttributes(), text, name);
        System.out.println(r + str);
    }
}

class XMLComposite extends XMLClass {

    List<XMLComponent> components;

    public XMLComposite(String name) {
        super(name);
        components = new ArrayList<>();
    }

    @Override
    public void addComponent(XMLComponent component) {
        components.add(component);
    }

    @Override
    public void print(int level) {
        String r = "    ".repeat(level);
        String str = String.format("<%s%s>",  name, getAttributes());
        System.out.println(r + str);
        components.forEach(c -> c.print(level+1));
        str = String.format("</%s>", name);
        System.out.println(r + str);
    }
}

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase==1) {
            component.print(0);
        } else if(testCase==2) {
            composite.print(0);
        } else if (testCase==3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level","1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level","2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level","3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            main.print(0);
        }
    }
}
