import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Slideshow {

    public List<Slide> slides;

    public Slideshow(Slide start) {
        this.slides = new ArrayList<>(Arrays.asList(start));
    }

    public void appendSlide(Slide slide) {
        slides.add(slide);
    }

    public String createOutput() {
        String out = slides.size() + "\n";
        out = slides.stream().map(slide -> {
            return slide.photos.stream()
                    .map(photo -> photo.id + "")
                    .reduce("", (acc, next) -> acc + next + " ");
        }).reduce(out, (acc, next) -> {
            return acc + next + "\n";
        });
        return out;
    }
}
