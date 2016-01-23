package sg.gempack.app.Utilities;

import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by xiend_000 on 24/1/2016.
 */
public class ParseACLHelper {

    public static ParseACL setParseObjectPermissions(){
        ParseACL parseUserPermissions = new ParseACL(ParseUser.getCurrentUser());
        parseUserPermissions.setPublicReadAccess(true);
        parseUserPermissions.setPublicWriteAccess(false);
        return parseUserPermissions;
    }

}
