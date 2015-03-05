package api;

import java.util.Base64;

public class Authenticate{

    //Checking Basic Authentication
    public static Boolean isValid(String auth){
        if (auth != null && auth.startsWith("Basic") ){

            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials.getBytes()));
            String[] values = credentials.split(":", 2);
            if(values[0].equals("foo") && values[1].equals("bar")){
                  return true;
            }
        }
        return false;
    }
}