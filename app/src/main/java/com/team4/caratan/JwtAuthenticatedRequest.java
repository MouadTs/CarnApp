package com.team4.caratan;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticatedRequest extends StringRequest {
    
    private static final String TAG = "JwtAuthenticatedRequest";
    private Context context;
    
    public JwtAuthenticatedRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Context context) {
        super(method, url, listener, errorListener);
        this.context = context;
    }
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        
        // Only set Content-Type to application/json if no form parameters are being sent
        // If getParams() is overridden, Volley will automatically set the correct content type
        try {
            Map<String, String> params = getParams();
            if (params == null || params.isEmpty()) {
                headers.put("Content-Type", "application/json");
            }
        } catch (AuthFailureError e) {
            // If getParams() throws an error, assume no form parameters
            headers.put("Content-Type", "application/json");
        }
        
        // Add JWT token if available
        String token = SharedPrefManager.getInstance(context).getJwtToken();
        if (token != null && !token.isEmpty()) {
            headers.put("Authorization", "Bearer " + token);
            Log.d(TAG, "JWT Token added to request: " + token.substring(0, Math.min(20, token.length())) + "...");
        } else {
            Log.w(TAG, "No JWT token found in SharedPreferences");
        }
        
        Log.d(TAG, "Request headers: " + headers);
        return headers;
    }
} 