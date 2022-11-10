import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Product {

    private final Map<RessourceType, Integer> neededResourcesMap;
    private final int point;
    private final String type;

    public Product(int point, String type) {
        this.point = point;
        this.type = type;
        neededResourcesMap = Collections.synchronizedMap(new HashMap<>());
    }

    public Map<RessourceType, Integer> getNeededResourcesMap() {
        return Collections.unmodifiableMap(neededResourcesMap);
    }

    public int getPoint() {
        return point;
    }

    public String getType() {
        return type;
    }
}
