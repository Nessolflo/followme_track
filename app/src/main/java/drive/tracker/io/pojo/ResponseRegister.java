package drive.tracker.io.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseRegister {
    @Expose
    @SerializedName("mensaje")
    public String message;
    @Expose
    @SerializedName("resultado")
    public boolean result;
    @Expose
    @SerializedName("idunidad")
    public int id;
}
