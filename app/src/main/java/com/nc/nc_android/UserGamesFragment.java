package com.nc.nc_android;

import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nc.nc_android.adapter.GameInfoAdapter;
import com.nc.nc_android.behavior.RSBlurProcessor;
import com.nc.nc_android.behavior.RecyclerItemSingleTapListener;
import com.nc.nc_android.dto.GameEditorInfoDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.pojo.GameInfo;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserGamesFragment extends NamedFragment {

    public UserGamesFragment() {
    }

    ArrayList<GameInfo> gameInfos;
    View root;
    RecyclerView rvGames;
    CardView cardViewGame;
    TextView txtGameName;
    TextView descriptionText;
    TextView maxPlayersText;
    TextView modeText;
    TextView storeTxt;
    TextView newTxt;
    Button editBtn;
    Button createBtn;
    Button deleteBtn;
    FloatingActionButton newBtn;
    FloatingActionButton storeBtn;
    RelativeLayout bkgLayout;
    EditorApi api;
    Boolean isNewBtnClicked;
    Long selectedId;
    GameInfoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.fragment_user_games, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Редактор");

        api = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class);

        rvGames = (RecyclerView) root.findViewById(R.id.rvGames);
        cardViewGame = (CardView) root.findViewById(R.id.game_card_view);
        txtGameName = (TextView) root.findViewById(R.id.game_info_text);
        bkgLayout = (RelativeLayout) root.findViewById(R.id.bkg_layout);
        descriptionText = (TextView) root.findViewById(R.id.descriptionTextCard);
        maxPlayersText = (TextView) root.findViewById(R.id.maxPlayersCard);
        modeText = (TextView) root.findViewById(R.id.gameModeCard);
        newBtn = (FloatingActionButton) root.findViewById(R.id.fbaNewGame);
        createBtn = (Button) root.findViewById(R.id.create_button_card);
        editBtn = (Button) root.findViewById(R.id.edit_button_card);
        deleteBtn = (Button) root.findViewById(R.id.delete_button_card);
        storeBtn = (FloatingActionButton) root.findViewById(R.id.fbaStore);
        storeTxt = (TextView) root.findViewById(R.id.storeText);
        newTxt = (TextView) root.findViewById(R.id.newText);

        cardViewGame.setVisibility(View.INVISIBLE);
        storeBtn.setVisibility(View.INVISIBLE);
        newTxt.setVisibility(View.INVISIBLE);
        storeTxt.setVisibility(View.INVISIBLE);
        isNewBtnClicked = false;
        descriptionText.setMovementMethod(new ScrollingMovementMethod());

        getGamesInfoFromServer();
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(MyApplication.getContext(), DividerItemDecoration.VERTICAL);
        rvGames.addItemDecoration(itemDecoration);
        rvGames.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        cardViewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bkgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewGame.setVisibility(View.INVISIBLE);
                fadeOutAndHideLayout(bkgLayout);
                bkgLayout.setVisibility(View.INVISIBLE);
                storeBtn.hide();
                if (newBtn.getVisibility() == View.INVISIBLE) {
                    newBtn.setVisibility(View.VISIBLE);
                }
                newTxt.setVisibility(View.INVISIBLE);
                storeTxt.setVisibility(View.INVISIBLE);
                isNewBtnClicked = false;
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.deleteGame(selectedId, UserData.loadUserId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        for (GameInfo game:gameInfos) {
                            if (game.getId() == selectedId){
                                int index = gameInfos.indexOf(game);
                                gameInfos.remove(game);
                                adapter.notifyItemRemoved(index);
                                cardViewGame.setVisibility(View.INVISIBLE);
                                fadeOutAndHideLayout(bkgLayout);
                                bkgLayout.setVisibility(View.INVISIBLE);
                                storeBtn.hide();
                                if (newBtn.getVisibility() == View.INVISIBLE) {
                                    newBtn.setVisibility(View.VISIBLE);
                                }
                                newTxt.setVisibility(View.INVISIBLE);
                                storeTxt.setVisibility(View.INVISIBLE);
                                isNewBtnClicked = false;
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new StoreFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra("gameId", selectedId);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new GameInfoFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewBtnClicked){
                    api.createNewEmptyGame(UserData.loadUserId()).enqueue(new Callback<Long>() {
                        @Override
                        public void onResponse(Call<Long> call, Response<Long> response) {
                            getActivity().getIntent().putExtra("gameId", response.body());
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, new GameInfoFragment())
                                    .addToBackStack(null)
                                    .commit();
                        }

                        @Override
                        public void onFailure(Call<Long> call, Throwable t) {

                        }
                    });

                } else {
                    Bitmap currentScreen = RSBlurProcessor.getBitmapFromView(root);
                    RSBlurProcessor rsBlurProcessor = new RSBlurProcessor(RenderScript.create(MyApplication.getContext()));
                    Bitmap blurredBkg = rsBlurProcessor.blur(currentScreen,1, 20);
                    bkgLayout.setBackground(new BitmapDrawable(blurredBkg));
                    bkgLayout.bringToFront();
                    bkgLayout.setVisibility(View.VISIBLE);
                    fadeInAndShowLayout(bkgLayout);
                    storeBtn.show();
                    newTxt.setVisibility(View.VISIBLE);
                    storeTxt.setVisibility(View.VISIBLE);
                    storeTxt.bringToFront();
                    newTxt.bringToFront();
                    newBtn.bringToFront();
                    storeBtn.bringToFront();
                }
                isNewBtnClicked = !isNewBtnClicked;
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra("gameId", selectedId);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new CreateInstanceFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        rvGames.addOnItemTouchListener(new RecyclerItemSingleTapListener(MyApplication.getContext(), new RecyclerItemSingleTapListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bitmap currentScreen = RSBlurProcessor.getBitmapFromView(root);
                RSBlurProcessor rsBlurProcessor = new RSBlurProcessor(RenderScript.create(MyApplication.getContext()));
                Bitmap blurredBkg = rsBlurProcessor.blur(currentScreen,1, 20);
                bkgLayout.setBackground(new BitmapDrawable(blurredBkg));
                bkgLayout.bringToFront();
                bkgLayout.setVisibility(View.VISIBLE);
                newBtn.setVisibility(View.INVISIBLE);
                fadeInAndShowLayout(bkgLayout);

                cardViewGame.setVisibility(View.VISIBLE);
                cardViewGame.bringToFront();
                txtGameName.setText(gameInfos.get(position).getName());
                modeText.setText(gameInfos.get(position).getMode());
                descriptionText.setText(gameInfos.get(position).getDescription());
                maxPlayersText.setText(gameInfos.get(position).getMaxPlayers() + "");
                selectedId = gameInfos.get(position).getId();
            }
        }));
        return root;
    }

    private void getGamesInfoFromServer(){
        final ArrayList<GameInfo> info = new ArrayList<>();
        api.getUserTemplates(UserData.loadUserId()).enqueue(new Callback<List<GameEditorInfoDto>>() {
            @Override
            public void onResponse(Call<List<GameEditorInfoDto>> call, Response<List<GameEditorInfoDto>> response) {
                for (GameEditorInfoDto game:response.body()) {
                    GameInfo gameInfo = new GameInfo(game.getName(), game.getDescription(), game.getMode().getName(), game.getMaxPlayers(), game.getId());
                    info.add(gameInfo);
                }
                initializeRecyclerView(info);
            }

            @Override
            public void onFailure(Call<List<GameEditorInfoDto>> call, Throwable t) {
            }
        });
    }

    private void initializeRecyclerView(ArrayList<GameInfo> infos){
        gameInfos = infos;
        adapter = new GameInfoAdapter(MyApplication.getContext(), gameInfos);
        rvGames.setAdapter(adapter);
    }

    private void fadeOutAndHideLayout(final RelativeLayout layout)
    {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(300);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                layout.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        layout.startAnimation(fadeOut);
    }
    private void fadeInAndShowLayout(final RelativeLayout layout)
    {
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(300);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                //layout.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        layout.startAnimation(fadeOut);
    }

    @Override
    public String getName() {
        return "Test";
    }
}
