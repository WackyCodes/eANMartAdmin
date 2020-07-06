package wackycodes.ecom.eanmartadmin.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import wackycodes.ecom.eanmartadmin.admin.AreaCodeCityModel;

public class DBQuery {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static final ArrayList<AreaCodeCityModel> areaCodeCityModelList = new ArrayList <>();




}
