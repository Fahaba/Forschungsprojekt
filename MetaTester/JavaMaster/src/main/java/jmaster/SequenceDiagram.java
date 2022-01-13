package jmaster;

import java.util.ArrayList;

public class SequenceDiagram {

    public static ArrayList<ArrayList<Object>> picassoWorks = new ArrayList<ArrayList<Object>>() {{
        add(new ArrayList<Object>() {{
            add("Pablo Picasso");
        }});
        add(new ArrayList<Object>() {{
            add("Guernica");
            add(1937);
        }});
        add(new ArrayList<Object>() {{
            add("Bull's Head");
            add(1942);
            add(45);
        }});
    }};

    public static ArrayList<ArrayList<Object>> daVinciWorks = new ArrayList<ArrayList<Object>>() {{
        add(new ArrayList<Object>() {{
            add("Leonardo da Vinci");
        }});
        add(new ArrayList<Object>() {{
            add("Mona Lisa");
            add(1505);
        }});
        add(new ArrayList<Object>() {{
            add("The Virgin and the Laughing Childd");
            add(1472);
            add(50);
        }});
        add(new ArrayList<Object>() {{
            add("The Last Supper");
            add(1498);
        }});
    }};

    public static void main(String[] argv) {
        JavaMaster picasso = new JavaMaster(picassoWorks.get(0).get(0).toString());

        // use() starts here
        helper(picasso, picassoWorks);
        JavaMaster daVinci = new JavaMaster(daVinciWorks.get(0).get(0).toString());
        helper(daVinci, daVinciWorks);
        // use() ends here
    }

    public static void helper(JavaMaster artist, ArrayList<ArrayList<Object>> objects) {
        for (ArrayList<Object> work: objects) {
            if (work.size() == 2) artist.createPainting(work.get(0).toString(), (Integer) work.get(1));
            if (work.size() == 3) artist.createSculpture(work.get(0).toString(), (Integer) work.get(1), (Integer) work.get(2));
        }

        artist.printArt();
        JavaMaster.greetings();
    }
}
