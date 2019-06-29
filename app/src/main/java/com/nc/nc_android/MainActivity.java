package com.nc.nc_android;


import android.content.Intent;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.nc.nc_android.background.NotificationService;
import com.nc.nc_android.dto.PersonDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.AuthorizationApi;
import com.nc.nc_android.retrofit.OrganizerApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;
import com.nc.nc_android.screen.game.GameFragment;
import com.nc.nc_android.screen.gamelist.GameListFragment;
import com.nc.nc_android.screen.validation.ValidationFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    Toolbar toolbar;

    Drawer drawer;
    AccountHeader headerResult;
    ProfileDrawerItem defaultAcc = new ProfileDrawerItem();

    FragmentManager fragmentManager;

    Fragment demoFragment;
    // TODO: 18.11.2017 Сделать нормальное переключение фрагментов из других фрагментов
    NamedFragment gameListFragment;
    NamedFragment gameFragment;
    Fragment demoButtonsFragnemt;
    Fragment editorFragment;
    Fragment validationFragment;
    NamedFragment userGamesFragment;

    NamedFragment currentFragment;

    AuthorizationApi authorizationApi;
    OrganizerApi organizerApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authorizationApi = RetrofitSingleton.getInstance(getApplicationContext()).getApi(AuthorizationApi.class);
        organizerApi = RetrofitSingleton.getInstance(getApplicationContext()).getApi(OrganizerApi.class);

        long id = UserData.loadUserId();
        if(id != -1){

            authorizationApi.findById(id).enqueue(new Callback<PersonDto>() {
                @Override
                public void onResponse(Call<PersonDto> call, Response<PersonDto> response) {
                    continueCreating(response.body(), savedInstanceState);
                }
                @Override
                public void onFailure(Call<PersonDto> call, Throwable t) {}
            });

            if(UserData.loadGameId() == -1){
                organizerApi.gameId(id).enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        if (response.code() == 200){
                            long userid = response.body();
                            if(userid < 0){
                                userid *= -1;
                                UserData.saveBoolean("isOverseer", true);
                            }
                            UserData.saveGameId(userid);
                        }
                    }
                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {}
                });

            }

        }else{

            Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent1);

        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();


            demoFragment = new DemoFragment();
            gameListFragment = new GameListFragment();
            gameFragment = new GameFragment();
            demoButtonsFragnemt = new DemoButtonsFragment();
            editorFragment = new EditorFragment();
            validationFragment = new ValidationFragment();
            userGamesFragment = new UserGamesFragment();






    }

    private void continueCreating(PersonDto person, Bundle savedInstanceState){

        defaultAcc
                .withEmail(person.getEmail())
                .withName(person.getName())
                .withIcon(Utils.getImageUrl(person.getPhoto()));

        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);

        initDrawer();

        if(savedInstanceState == null){
            String page = getIntent().getStringExtra("page");
            if(page != null) switch (page){
                case "welcome": setFragment(gameListFragment, "Квесты"); drawer.setSelection(2); break;
                case "game": setFragment(gameFragment, "game"); drawer.setSelection(1); break;
            }
        }

    }

    private void initDrawer() {

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(defaultAcc)
                .build();

        final PrimaryDrawerItem itemGame = new PrimaryDrawerItem().withIdentifier(1).withName("Текущая игра");
        final PrimaryDrawerItem itemGameList = new PrimaryDrawerItem().withIdentifier(2).withName("Квесты");
        final PrimaryDrawerItem itemDemo = new PrimaryDrawerItem().withIdentifier(3).withName("Демо");
        final PrimaryDrawerItem itemQuest = new PrimaryDrawerItem().withIdentifier(4).withName("Квесты");
        final PrimaryDrawerItem itemButtons = new PrimaryDrawerItem().withIdentifier(5).withName("Кнопочки");
        final PrimaryDrawerItem itemEditor = new PrimaryDrawerItem().withIdentifier(6).withName("Редактор");
        final PrimaryDrawerItem itemUserGames = new PrimaryDrawerItem().withIdentifier(9).withName("Редактор квестов");

        final PrimaryDrawerItem itemValidation = new PrimaryDrawerItem().withIdentifier(7).withName("Проверить задание");
        final PrimaryDrawerItem itemLogout = new PrimaryDrawerItem().withIdentifier(8).withName("Выйти");

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        itemGame,
                        itemGameList,
                        //itemDemo,
                        //itemQuest,
                        //itemButtons,
                        //itemEditor,
                        //itemValidation,
                        itemUserGames,
                        new DividerDrawerItem(),
                        itemLogout

                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {

                    if(drawerItem == itemUserGames){setFragment(userGamesFragment, "test");}
                  //  if(drawerItem == itemDemo){setFragment(demoFragment, "demo");}
                    if(drawerItem == itemGameList){setFragment(gameListFragment, "Квесты");}
                    if(drawerItem == itemQuest){setFragment(gameListFragment, "gamelist");}
                    if(drawerItem == itemGame){setFragment(gameFragment, "game");}
                  //  if(drawerItem == itemButtons){setFragment(demoButtonsFragnemt, "buttons");}
                  //  if(drawerItem == itemEditor){setFragment(editorFragment, "editor");}
                   // if(drawerItem == itemValidation){setFragment(validationFragment, "validation");}

                    if(drawerItem == itemLogout){
                        UserData.saveUserId(-1);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }

                    drawer.closeDrawer();

                    return true;
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {

                        if(UserData.loadGameId() == -1){

                            itemGame.withEnabled(false);

                            organizerApi.gameId(UserData.loadUserId()).enqueue(new Callback<Long>() {
                                @Override
                                public void onResponse(Call<Long> call, Response<Long> response) {
                                    if (response.code() == 200){
                                        long userid = response.body();

                                        UserData.saveBoolean("isOverseer", userid < 0);
                                        if(userid < 0){
                                            userid *= -1;
                                        }
                                        UserData.saveGameId(userid);
                                        itemGame.withEnabled(true);
                                        drawer.updateItem(itemGame);
                                    }
                                }
                                @Override
                                public void onFailure(Call<Long> call, Throwable t) {}
                            });

                        }else{
                            itemGame.withEnabled(true);
                        }
                        drawer.updateItem(itemGame);
                    }
                    @Override
                    public void onDrawerClosed(View drawerView) {}
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {}
                })
                .build();

    //    drawer.setSelection(2);
    }

    @Override
    public void onBackPressed(){
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            if(drawer.isDrawerOpen()){
                drawer.closeDrawer();
            }
            else{
                super.onBackPressed();
            }
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public void setFragment(NamedFragment fragment, String tag){

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, fragment.getName())
                .addToBackStack(null)
                .commitAllowingStateLoss();
        toolbar.setTitle(fragment.getName());
        currentFragment = fragment;
        invalidateOptionsMenu();
    }

    public void showHome(){
        setFragment(gameListFragment, "Квесты");
        drawer.setSelection(2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case R.id.action_exit:
                ((GameFragment) gameFragment).exitButtonHandle();
                break;
            case R.id.action_about:
                ((GameFragment) gameFragment).aboutButtonHandle();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem register = menu.findItem(R.id.action_exit);
        register.setVisible(currentFragment == gameFragment);
        register = menu.findItem(R.id.action_about);
        register.setVisible(currentFragment == gameFragment);
        return true;
    }


}
