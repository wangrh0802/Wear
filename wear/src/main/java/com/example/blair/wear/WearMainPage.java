package com.example.blair.wear;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.view.WatchViewStub;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blair.wear.utils.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class WearMainPage extends Activity implements GoogleApiClient.ConnectionCallbacks
        ,DataApi.DataListener, MessageApi.MessageListener, View.OnClickListener{

    public static final int WAITING = 0;
    public static final int PREPARED = 1;
    public static final int PHONE_SELECTED = 2;
    public static final int WEAR_SELECTED = 3;

    public static final String JOIN_PATH = "/join";
    public static final String JOIN_RESULT_PATH = "/join_result";
    public static final String GAME_PATH = "/game";
    public static final String RECORD_PATH = "/record";
    public static final String RESULT_PATH = "/result";

    private int gameStatus;
    private String phoneChoice = "";
    private String wearChoice = "";
    private List<Node> mConnectedNodes;

    private TextView gameTip;
    private TextView gameResultRecord;
    private ImageView rock;
    private ImageView paper;
    private ImageView scissors;
    private Dialog dialog;
    private TextView dialogOk;
    private ImageView wearChoiceImage;
    private ImageView phoneChoiceImage;
    private TextView dialogResult;


    private GoogleApiClient googleApiClient;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 9999:
                    Wearable.MessageApi.sendMessage(googleApiClient, mConnectedNodes.get(0).getId(),
                            JOIN_PATH, (PREPARED + "").getBytes());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                gameTip = (TextView) stub.findViewById(R.id.game_tip);
                gameResultRecord = (TextView) stub.findViewById(R.id.game_result_record);
                rock = (ImageView) stub.findViewById(R.id.rock);
                rock.setOnClickListener(WearMainPage.this);
                paper = (ImageView) stub.findViewById(R.id.paper);
                paper.setOnClickListener(WearMainPage.this);
                scissors = (ImageView) stub.findViewById(R.id.scissors);
                scissors.setOnClickListener(WearMainPage.this);
                resetImage();
                gameStatus = WAITING;
                changeTip();
            }
        });

        View view = LayoutInflater.from(this).inflate(R.layout.result_dialog, null);
        dialogResult = (TextView)view.findViewById(R.id.game_result_text);
        dialogOk = (TextView)view.findViewById(R.id.game_result_back);
        dialogOk.setOnClickListener(this);
        wearChoiceImage = (ImageView)view.findViewById(R.id.wear_choice);
        phoneChoiceImage = (ImageView)view.findViewById(R.id.phone_choice);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        if(gameStatus == WAITING){
            Toast.makeText(this, "disconnect", Toast.LENGTH_SHORT).show();
            return;
        }

        if(gameStatus == WEAR_SELECTED){
            return;
        }

        switch (v.getId()){
            case R.id.rock:
                rock.setAlpha(1.0f);
                sendToPhone(Util.ROCK);
                break;
            case R.id.paper:
                paper.setAlpha(1.0f);
                sendToPhone(Util.PAPER);
                break;
            case R.id.scissors:
                scissors.setAlpha(1.0f);
                sendToPhone(Util.SCISSORS);
                break;
            case R.id.game_result_back:
                if(dialog.isShowing())
                    dialog.dismiss();
                break;
        }
    }

    private void sendToPhone(String item){

        wearChoice = item;
        Wearable.MessageApi.sendMessage(googleApiClient, mConnectedNodes.get(0).getId(),
                GAME_PATH, item.getBytes());
        gameStatus = WEAR_SELECTED;
        changeTip();
        if(TextUtils.isEmpty(phoneChoice))
            return;

        int result = Util.getWearResult(item, phoneChoice);
        showResult(result);
        Wearable.MessageApi.sendMessage(googleApiClient, mConnectedNodes.get(0).getId(),
                RESULT_PATH, (result + "").getBytes());
    }


    private void showResult(int result){
        if(result == Util.WIN){

            dialogResult.setText("You Win");
            wearChoiceImage.setImageResource(Util.getImageIdByName(wearChoice));
            phoneChoiceImage.setImageResource(Util.getImageIdByName(phoneChoice));
            dialog.show();
            resetGame();

        }else if(result == Util.TIE){

            dialogResult.setText("You Tie");
            wearChoiceImage.setImageResource(Util.getImageIdByName(wearChoice));
            phoneChoiceImage.setImageResource(Util.getImageIdByName(phoneChoice));
            dialog.show();
            resetGame();

        }else if(result == Util.LOSE){

            dialogResult.setText("You Lose");
            wearChoiceImage.setImageResource(Util.getImageIdByName(wearChoice));
            phoneChoiceImage.setImageResource(Util.getImageIdByName(phoneChoice));
            dialog.show();
            resetGame();
        }
    }

    private void changeTip(){

        switch (gameStatus){
            case PREPARED:
                gameTip.setText("Prepared, please make your choice");
                break;

            case WEAR_SELECTED:
                gameTip.setText("You have selected, waiting for phone");
                break;

            case PHONE_SELECTED:
                gameTip.setText("Phone have selected, please make your choice");
                break;

            default:
                gameTip.setText("Waiting phone to connect");
                break;
        }

    }

    private void resetGame(){
        phoneChoice = "";
        wearChoice = "";
        resetImage();
        gameStatus = PREPARED;
        changeTip();
    }

    private void resetImage(){
        rock.setAlpha(0.3f);
        paper.setAlpha(0.3f);
        scissors.setAlpha(0.3f);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(googleApiClient, this);
        Wearable.MessageApi.addListener(googleApiClient, this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mConnectedNodes = getNodes();
                handler.sendEmptyMessage(9999);
            }
        }).start();
        gameStatus = PREPARED;
        changeTip();
    }

    private List<Node> getNodes() {
        List<Node> results= new ArrayList<>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
        for (Node node : nodes.getNodes()) {
            results.add(node);
        }
        return results;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gameStatus != WAITING) {
            Wearable.MessageApi.sendMessage(googleApiClient, mConnectedNodes.get(0).getId(),
                    JOIN_PATH, (WAITING + "").getBytes());
        }
        if ((null != googleApiClient) && (googleApiClient.isConnected())) {
            Wearable.DataApi.removeListener(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String path = messageEvent.getPath();
        if(path.equals(GAME_PATH)){

            phoneChoice = new String(messageEvent.getData());
            gameStatus = PHONE_SELECTED;
            changeTip();

        } else if(path.equals(RECORD_PATH)){

            String record = new String(messageEvent.getData());
            String[] results = record.split("%%");
            gameResultRecord.setText("WIN: "+results[2]+", TIE: "+results[1]+", LOSE: "+results[0]);

        }else if(path.equals(RESULT_PATH)){

            int result = Integer.parseInt(new String(messageEvent.getData()));
            if(result == Util.LOSE)
                showResult(Util.WIN);
            else if(result == Util.TIE)
                showResult(Util.TIE);
            else if(result == Util.WIN)
                showResult(Util.LOSE);

        }else if(path.equals(JOIN_PATH)){

            gameStatus = Integer.parseInt(new String(messageEvent.getData()));
            changeTip();
            Wearable.MessageApi.sendMessage(googleApiClient, mConnectedNodes.get(0).getId(),
                    JOIN_RESULT_PATH, (gameStatus + "").getBytes());

        }else if(path.equals(JOIN_RESULT_PATH)){

            gameStatus = Integer.parseInt(new String(messageEvent.getData()));
            changeTip();
        }
    }
}
