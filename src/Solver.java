import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Solver {

    public List<Photo> photos;

    public Solver(String input) {
        List<String> lines = new ArrayList<>(Arrays.asList(input.split("\n")));
        lines.remove(0);

        photos = new ArrayList<>();
        for (int i=0; i<lines.size(); i++) {
            Photo p = new Photo(lines.get(i), i);
            photos.add(p);
        }

    }

    public String solve() {
        Map<String, Set<Slide>> map = new HashMap<>();

        List<Slide> verticalSlides = pairAllVerticals();

        List<Slide> horizontalSlides = photos.stream()
                .filter(photo -> photo.orientation == 'H')
                .map(Arrays::asList)
                .map(Slide::new)
                .collect(Collectors.toList());

        List<Slide> allSlides = new ArrayList<>(verticalSlides);
        allSlides.addAll(horizontalSlides);

        allSlides.forEach(slide -> {
            slide.getTags().forEach(tag -> {
                if (!map.containsKey(tag)) {
                    map.put(tag, new HashSet<>());
                }
                map.get(tag).add(slide);
            });
        });

        Slide current = allSlides.get(0);
        current.used = true;
        Slideshow slideshow = new Slideshow(current);

        while (true) {
            System.out.println("Slideshow size: " + slideshow.slides.size());

            int bestScore = 0;
            Slide bestScoreSlide = null;

            int iteration = 0;

            for (String tag : current.getTags()) {
                for (Slide candidate : map.get(tag)) {
                    if (candidate.used) continue;
                    if (iteration > 3000) break;
                    iteration++;
                    int candidateScore = getScore(current, candidate);
                    if (candidateScore > bestScore) {
                        bestScore = candidateScore;
                        bestScoreSlide = candidate;
                    }
                }
                if (iteration > 3000) break;
            }

//            Set<Slide> candidates = current.getTags().stream()
//                    .map(map::get)
//                    .reduce(new HashSet<>(), (acc, next) -> {
//                        acc.addAll(next);
//                        return acc;
//                    });


            //Set<Slide> slidesWithCurrentTag = map.get(tag);
//            int iteration = 0;
//            for (Slide candidate : candidates) {
//                if (iteration > 20) break;
//                iteration++;
//                if (candidate.used) continue;
//
//                int candidateScore = getScore(current, candidate);
//                if (candidateScore > bestScore) {
//                    bestScore = candidateScore;
//                    bestScoreSlide = candidate;
//                }
//            }

            if (bestScoreSlide == null) {
                System.out.println("Slideshow size: " + slideshow.slides.size());
                for (Slide slide : allSlides) {
                    if (!slide.used) {
                        bestScoreSlide = slide;
                        break;
                    }
                }
            }

            if (bestScoreSlide == null) {
                break;
            }

            bestScoreSlide.used = true;

            for (String tag : bestScoreSlide.getTags()) {
                map.get(tag).remove(bestScoreSlide);
            }

            slideshow.appendSlide(bestScoreSlide);
            current = bestScoreSlide;
        }

        return slideshow.createOutput();
    }

    private List<Slide> pairAllVerticals() {
        List<Photo> verticalPhotos = photos.stream()
                .filter(photo -> photo.orientation == 'V')
                .collect(Collectors.toList());

        List<Slide> verticalSlides = new ArrayList<>();
        int depth = 2000;
        for (int i = 0; i < verticalPhotos.size(); i++) {
            Photo left = verticalPhotos.get(i);
            Photo bestMatch = null;
            int bestScore = 0;
            if (left.paired) continue;
            for (int j = 0; j < depth; j++) {
                Photo right = verticalPhotos.get((i + j) % verticalPhotos.size());
                if (right.paired) continue;
                int score = verticalHeuristic(left, right);
                if (score > bestScore) {
                    bestScore = score;
                    bestMatch = right;
                }
            }
            if (bestMatch == null) {
                for (Photo right : verticalPhotos) {
                    if (!right.paired) {
                        bestMatch = right;
                        break;
                    }
                }
            }
            left.paired = true;
            bestMatch.paired = true;
            Slide slide = new Slide(Arrays.asList(left, bestMatch));
            verticalSlides.add(slide);
        }
//        List<Slide> verticalSlides = new ArrayList<>();
//
//        for (int i=0; i<verticalPhotos.size(); i+=2) {
//            Photo left = verticalPhotos.get(i);
//            Photo right = verticalPhotos.get(i+1);
//            Slide slide = new Slide(Arrays.asList(left, right));
//            verticalSlides.add(slide);
//        }

        return verticalSlides;
    }

    private int verticalHeuristic(Photo left, Photo right) {
        if (left.tags.size() > right.tags.size()) {
            Photo tmp = left;
            left = right;
            right = tmp;
        }

        int intersection = 0;
        for (String tag : left.tags) {
            if (right.tags.contains(tag)) {
                intersection++;
            }
        }

        return left.tags.size() + right.tags.size() - 2 * intersection;
    }

    private int getScore(Slide lhs, Slide rhs) {
        Set<String> lhsCopy = lhs.getTags();
        Set<String> rhsCopy = rhs.getTags();

        if (lhsCopy.size() > rhsCopy.size()) {
            Set<String> tmp = lhsCopy;
            lhsCopy = rhsCopy;
            rhsCopy = tmp;
        }

        int intersection = 0;
        for (String tag : lhsCopy) {
            if (rhsCopy.contains(tag)) intersection++;
        }

        int lhsOnly = lhsCopy.size() - intersection;
        int rhsOnly = rhsCopy.size() - intersection;

        return Math.min(Math.min(lhsOnly, rhsOnly), intersection);
    }

}
