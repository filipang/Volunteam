package com.volunteam.components;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.internal.Util;
import com.volunteam.R;
import com.volunteam.activities.MainActivity;
import com.volunteam.activities.ValidateActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class FirebaseHandler {

    static private FirebaseHandler firebaseHandler;
    static private Uri filePath ;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    public DataSnapshot data;
    private static FirebaseStorage storage;
    private static StorageReference storageReference;

    public FirebaseHandler(FirebaseAuth auth, FirebaseUser user, FirebaseDatabase database, DatabaseReference reference) {
        this.auth = auth;
        this.user = user;
        this.database = database;
        this.reference = reference;
    }

    public static FirebaseHandler getFirebaseHandler() {
        return firebaseHandler;
    }

    public static void setFirebaseHandler(FirebaseHandler firebaseHandler) {
        FirebaseHandler.firebaseHandler = firebaseHandler;
        downloadDataSet();
    }

    public static void downloadDataSet(){
        //DOWNLOAD DATABASE HERE//

        firebaseHandler.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadDataSet(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static void loadDataSet(DataSnapshot dataSnapshot){
        ArrayList<Voluntariat> voluntariatArrayList = new ArrayList<>();
        Log.d("HOW MANYY??", "THIS MANY: " + dataSnapshot.child("Voluntariate").getChildren());
        for (DataSnapshot ds : dataSnapshot.getChildren()) Log.d("debugare", ds.getKey());
        for(DataSnapshot ds : dataSnapshot.child("Voluntariate").getChildren()){
            Log.d("Children", "WE GOT +ONE!");
            Voluntariat vol = new Voluntariat();

            Log.d("testingthis", ds.child("id_vol").getValue().toString());
            vol.setId_vol(Integer.parseInt(ds.child("id_vol").getValue().toString()));
            vol.setName(checkNull(ds.child("name").getValue().toString()));
            vol.setImageList(new ArrayList<String>());

            vol.setImageURL(ds.child("imageURL").getValue().toString());
            vol.getImageList().add(vol.getImageURL());

            if(URLUtil.isValidUrl(ds.child("imageURL1").getValue().toString())) vol.getImageList().add(ds.child("imageURL1").getValue().toString());
            if(URLUtil.isValidUrl(ds.child("imageURL2").getValue().toString())) vol.getImageList().add(ds.child("imageURL2").getValue().toString());
            if(URLUtil.isValidUrl(ds.child("imageURL3").getValue().toString())) vol.getImageList().add(ds.child("imageURL3").getValue().toString());
            if(URLUtil.isValidUrl(ds.child("imageURL4").getValue().toString())) vol.getImageList().add(ds.child("imageURL4").getValue().toString());
            if(URLUtil.isValidUrl(ds.child("imageURL5").getValue().toString())) vol.getImageList().add(ds.child("imageURL5").getValue().toString());
            vol.setDescription(checkNull(ds.child("description").getValue().toString()));
            vol.setDate(new Date(ds.child("day").getValue().toString(), ds.child("month").getValue().toString(),  ds.child("year").getValue().toString()));
            vol.setId_user(checkNull(ds.child("organizer").getValue().toString()));
            vol.setLink(ds.child("link").getValue().toString());
            ArrayList<String> user_list = new ArrayList<>();
            if(ds.child("users").getChildren()!=null) {
                for (DataSnapshot snapshot : ds.child("users").getChildren()) {
                    user_list.add(checkNull(snapshot.getValue().toString()));
                }
            }
            vol.userList = user_list;
            //vol.setLink(ds.child("link").getValue().toString());
            vol.setDrawable(Voluntariat.loadDrawableFromURL(vol.getImageURL()));
            voluntariatArrayList.add(vol);
        }
        Voluntariat.setDataSet(voluntariatArrayList);
    }

    public static void refreshSmallAdapter(final SmallRecyclerAdapter adapter, final SwipeRefreshLayout refreshLayout, final  SearchBarManager searchBarManager){
        firebaseHandler.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadDataSet(dataSnapshot);
                adapter.update();
                SortSpinnerManager.sortBy(SortSpinnerManager.getCurrentSort(),  User.getVolFromUser(FirebaseHandler.getFirebaseHandler().getData()));
                searchBarManager.searchSmall(SearchBarManager.getCurrentQuery(),  User.getVolFromUser(FirebaseHandler.getFirebaseHandler().getData()), adapter);
                SortSpinnerManager.sortBy(SortSpinnerManager.getCurrentSort(), User.getVolFromUser(FirebaseHandler.getFirebaseHandler().getData()));
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void uploadImageToFirebase(Intent intent, final AppCompatActivity context){
        Uri _imageUri = intent.getData();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if(_imageUri!= null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final String imageStoragePath = "images/"+ UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(imageStoragePath);
            ref.putFile(_imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                            Button button = context.findViewById(R.id.button_image_1);
                            button.setVisibility(View.GONE);
                            EditText text = context.findViewById(R.id.imageURL);
                            text.setEnabled(false);
                            text.setText(imageStoragePath);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            Button button = context.findViewById(R.id.button_image_1);
                            button.setText("Try again");
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }

    }

    private void uploadImage(final AppCompatActivity context) {
        filePath = Uri.parse("photos/");
        if(filePath != null)
        {
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public static void refreshLargeAdapter(final LargeRecyclerAdapter adapter, final SwipeRefreshLayout refreshLayout, final SearchBarManager searchBarManager){
        firebaseHandler.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadDataSet(dataSnapshot);
                adapter.update();
                SortSpinnerManager.sortBy(SortSpinnerManager.getCurrentSort(), (ArrayList<Voluntariat>) adapter.mDataSet);
                searchBarManager.searchLarge(SearchBarManager.getCurrentQuery(), Voluntariat.getDataSet(), adapter);
                SortSpinnerManager.sortBy(SortSpinnerManager.getCurrentSort(), (ArrayList<Voluntariat>) adapter.mDataSet);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void setVoluntariatValue(DatabaseReference reference, String id, String field, Object value){
        reference.child("Voluntariate").child(id).child(field).setValue(value);
    }

    public DataSnapshot getData() {
        return data;
    }

    public void setData(DataSnapshot data) {
        this.data = data;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public static String checkNull(String s){
        return s==null?"":s;
    }
}
