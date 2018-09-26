package sonu.kumar.jaquar.Singleton;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by sonu on 2/9/18.
 */

public class MySingleton {
    private static MySingleton mySingleton;
    private RequestQueue requestQueue;
    private  static Context ctx;
    private MySingleton(Context context){
        ctx =context;
        requestQueue = getRequestQueue();

    }
    public RequestQueue getRequestQueue(){
        if (requestQueue  == null){
            requestQueue = Volley.newRequestQueue(ctx);
        }
        return  requestQueue;
    }
    public static  synchronized MySingleton getInstance(Context context){
        if (mySingleton == null){
            mySingleton =new MySingleton(context);
        }
        return mySingleton;
    }
    public<T>  void  addToRequestQuee(Request<T> request){
        requestQueue.add(request);
    }
}
