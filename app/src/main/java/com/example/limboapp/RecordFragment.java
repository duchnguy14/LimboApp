package com.example.limboapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.icu.util.ValueIterator;
import android.media.Image;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_SHORT;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment implements SurfaceHolder.Callback{

    private OnFragmentInteractionListener mListener;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    final  int CAMERA_REQUEST_CODE = 1;
    int camBackId = Camera.CameraInfo.CAMERA_FACING_BACK;
    int camFrontId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    ImageView mRotate;
    ImageView mCapture;
    Camera camera;
    ImageView aSwitch;
    boolean isFlashOn = false;
    View view;
    boolean recording = false;
    MediaRecorder recorder;
    final static int REQUEST_VIDEO_CAPTURED = 1;
    String videoPath = "";
    FirebaseAuth mAuth;




    public RecordFragment() {


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance() {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        File file = new File(Environment.DIRECTORY_PICTURES,"Limbo");
        if(!file.exists()){
            Log.d("DEBUG", "newInstance: mkdirs");
            file.mkdirs();
        }


        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_record, container, false);
        mSurfaceView = view.findViewById(R.id.videoview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mCapture = view.findViewById(R.id.bt1);
        //mRotate = view.findViewById(R.id.bt2);
        aSwitch = view.findViewById(R.id.bt3);

        mAuth = FirebaseAuth.getInstance();



        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            Log.d("PAIGE", "onCreateView: HI");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE} , CAMERA_REQUEST_CODE);
        } else {
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setFormat(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }



       aSwitch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(isFlashOn){
                   off();
                   aSwitch.setImageResource(R.drawable.ic_flash_off);
               }
               else{
                   on();
                   aSwitch.setImageResource(R.drawable.ic_flash);
               }
           }
       });

        mCapture.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra("android.intent.extra.durationLimit",7);
                startActivityForResult(intent,REQUEST_VIDEO_CAPTURED);
                //TODO: publish activity?
                //                try {
//                    initRecorder();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });








        return view;
    }

    public  void on() {

        if(isFlashOn == true) {
            Toast.makeText(getContext(), "Flash is ON", LENGTH_SHORT).show();
        } else{
            Camera.Parameters parameters;
            parameters  = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            isFlashOn = true;

        }
    }
    public void off(){

        if(isFlashOn == false) {
            Toast.makeText(getContext(), "Flash is OFF", LENGTH_SHORT).show();
        } else{
            Camera.Parameters parameters;
            parameters  = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            isFlashOn = false;

        }


    }


    private void captureVideo() {
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
          camera =  Camera.open();
          Camera.Parameters parameters;
          parameters  = camera.getParameters();
          camera.setDisplayOrientation(90);
          parameters.setPreviewFrameRate(30);
          parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
          camera.setParameters(parameters);


        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mSurfaceHolder.addCallback(this);
                    mSurfaceHolder.setFormat(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }else{
                    Toast.makeText(getContext(), "Please Check Permissions", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
//    private void initRecorder() throws IOException{
//
//        recorder = new MediaRecorder();
//        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//       // recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//
//
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        //recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
//        recorder.setPreviewDisplay(mSurfaceHolder.getSurface());
//        recorder.setOutputFile(srcPath);
//        recorder.setMaxDuration(7000);
//        recorder.setMaxFileSize(5000000);
//        recorder.prepare();
//        recorder.start();
//    }
//    protected void stopRecording() {
//        recorder.stop();
//        recorder.release();
//        camera.release();
//    }
//
//    private void releaseMediaRecorder(){
//        if (recorder != null) {
//            recorder.reset();
//            recorder.release();
//            recorder = null;
//            camera.lock();
//        }
//    }

    private void releaseCamera(){
        if (camera != null){
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri videoUri = data.getData();
            Log.d("PAIGE", "onActivityResult: uri = " + videoUri.getPath());
            File videoFile =  new File(getPath(videoUri));
            Uri file = Uri.fromFile(videoFile);
            final StorageReference videoRef = FirebaseStorage.getInstance().getReference()
                    .child(file.getLastPathSegment());
            UploadTask uploadTask = videoRef.putFile(file);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("PAIGE", "onFailure: upload error: " + e);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageMetadata metadata = taskSnapshot.getMetadata();

                    //TODO: work here when merged
                    Task<Uri> downloadUrl = videoRef.getDownloadUrl();
                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String videoReference = uri.toString();
                            Log.d("PAIGE", "onSuccess: videoReference = " + videoReference);
                            Video video = new Video("My first video upload!",
                                    videoReference,
                                    mAuth.getCurrentUser().getDisplayName(),
                                    mAuth.getCurrentUser().getPhotoUrl().toString().replace("s96-c", "s400-c"),
                                    mAuth.getCurrentUser().getUid(),0);
                            DatabaseReference videoDataRef = FirebaseDatabase.getInstance().getReference().child("videos");

                            videoDataRef.push().setValue(video).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Your video has been uploaded!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.d("PAIGE", "error adding new video to database: " + task.getException());
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private String getPath(Uri videoUri) {
        String result;
        Cursor cursor = getContext().getContentResolver().query(videoUri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = videoUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        Log.d("PAIGE", "getPath: result = " + result);
        return result;
    }
}
