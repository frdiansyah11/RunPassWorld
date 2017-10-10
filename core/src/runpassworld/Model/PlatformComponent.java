package runpassworld.Model;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by Nicoy on 10/10/2017.
 */

    //class untuk membuat platform semisalkan bergerak vertikal

public class PlatformComponent implements Component {
    public static Vector2 originalPosition;
    public static float timePassed = 0;
}
