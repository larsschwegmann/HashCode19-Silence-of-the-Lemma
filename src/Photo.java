import java.util.*;

public class Photo {

    public Set<String> tags;
    public char orientation;
    public int id;
    public boolean paired;

    public Photo(String input, int id) {
        List<String> in = new ArrayList<>(Arrays.asList(input.split(" ")));
        orientation = in.get(0).charAt(0);
        this.id = id;
        in.remove(0);
        in.remove(0);
        tags = new HashSet<>(in);
    }
}
