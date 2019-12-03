package com.example.limboapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

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
    String srcPath = "/storage/emulated/0/DCIM/Limbo/test";





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






        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA} , CAMERA_REQUEST_CODE);
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
                startActivityForResult(intent,REQUEST_VIDEO_CAPTURED);
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

}
