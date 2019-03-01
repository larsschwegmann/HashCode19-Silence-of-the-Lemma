import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Slide {

    public List<Photo> photos;
    public boolean used;
    public int id;

    private Set<String> tagsCache = null;

    public Set<String> getTags() {
        if (tagsCache == null) {
            List<Set<String>> photoTagList = photos.stream()
                    .map(photo -> photo.tags)
                    .collect(Collectors.toList());

            Set<String> tags = new HashSet<>();
            photoTagList.forEach(tags::addAll);
            tagsCache = tags;
        }
        return tagsCache;
    }

    public Slide(List<Photo> photos) {
        this.photos = photos;
        this.id = id;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
