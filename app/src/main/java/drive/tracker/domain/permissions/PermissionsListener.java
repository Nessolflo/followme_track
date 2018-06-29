package drive.tracker.domain.permissions;

/**
 * Created by nesto on 2/08/2016.
 */
public interface PermissionsListener {
    void solicitarPermisos();
    boolean comprobarPermisos();
    boolean onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults);
}
