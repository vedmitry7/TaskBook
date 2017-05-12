package com.example.dmitryvedmed.taskbook.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dmitryvedmed.taskbook.logic.DBHelper5;
import com.example.dmitryvedmed.taskbook.R;
import com.example.dmitryvedmed.taskbook.logic.Section;
import com.example.dmitryvedmed.taskbook.logic.SuperTask;
import com.example.dmitryvedmed.taskbook.helper.SimpleItemTouchHelperCallback;
import com.example.dmitryvedmed.taskbook.helper.SpacesItemDecoration;
import com.example.dmitryvedmed.taskbook.untils.Constants;

import java.util.List;

public class DrawerTestActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private List<SuperTask> values;
    public static DBHelper5 dbHelper;
    public static RecyclerView recyclerView;
    private MainRecyclerAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelper.Callback callback;
    boolean is_in_action_mode = false;
    private  TextView counterTextView, mainToolbarText;
    private Toolbar toolbar, toolbar2;
    public String currentKind = Constants.UNDEFINED;
    private Context context;
    private Menu menu;
    private FloatingActionButton fab;
    private FloatingActionButton fabAddST;
    private FloatingActionButton fabAddLT;
    public static CoordinatorLayout coordinatorLayout;
    private MenuItem setColor, delete, choose, clearBascet, delateForever, cancelSelection;
    private Animation fabAddAnimetion, fabCancelAnimation, fabOpen, fabClose;
    private boolean fabPressed;
    private SharedPreferences sharedPreferences;



    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_test);
        context = this;
        dbHelper = new DBHelper5(this);
        loadPreferences();
        update();
        initView();
        initAnimation();
        loadPreferences();
    }

    private void loadPreferences(){
        sharedPreferences = this.getSharedPreferences(Constants.NAME_PREFERENCES, Context.MODE_PRIVATE);

    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabAddST = (FloatingActionButton) findViewById(R.id.fabAddST);
        fabAddLT = (FloatingActionButton) findViewById(R.id.fabAddLT);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("TAG", "                  touch toolbar)");
                hideFabs();
                return false;
            }
        });

        toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        toolbar2.setVisibility(View.GONE);
        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "       TOOLBAR 2 click");
                adapter.cancelSelection();
            }
        });


        counterTextView = (TextView) findViewById(R.id.counter_text2);
        //counterTextView.setVisibility(View.GONE);

        mainToolbarText = (TextView) findViewById(R.id.mainToolbarText);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_common);
        adapter = new MainRecyclerAdapter(values, DrawerTestActivity.this);
        RecyclerView.LayoutManager layoutManager;
        String s = sharedPreferences.getString(Constants.MAIN_RECYCLER_LAYOUT, Constants.LAYOUT_LIST);

        if(s.equals(Constants.LAYOUT_LIST)) {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new StaggeredGridLayoutManager(2, 1);
        }
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new SpacesItemDecoration(15));
        callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //  drawerLayout.setScrimColor(Color.TRANSPARENT);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "      TOOOOOGLE CLICK ---");
                hideFabs();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);


        setColor = menu.findItem(R.id.set_color);
        setColor.setVisible(false);
        delete = menu.findItem(R.id.delete_selection_items);
        delete.setVisible(false);
        choose = menu.findItem(R.id.select_item);
        clearBascet = menu.findItem(R.id.clear_basket);
        if(currentKind==Constants.DELETED && adapter.getTasks().size()!=0)
            clearBascet.setVisible(true);
        else
            clearBascet.setVisible(false);
        delateForever = menu.findItem(R.id.delete_forever);
        delateForever.setVisible(false);
        cancelSelection = menu.findItem(R.id.cancel_selection);
        cancelSelection.setVisible(false);
        //  menuItemDelete = menu.findItem(R.id.delete);
        //  menuItemDelete.setVisible(false);

        /*ArrayList<Section> sections = dbHelper.getAllSections();
        System.out.println("ssssssssssssssssss");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu navmenu = navigationView.getMenu();
        Menu submenu = navmenu.getItem(0).getSubMenu();
        submenu.clear();

        for (Section s:sections
             ) {
            System.out.println(s.getName());
            //MenuItem sections =  menu.getItem(R.id.sections);
            submenu.add(R.id.sections,Menu.FIRST,Menu.NONE, s.getName());
        }*/

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (adapter.getMode()== MainRecyclerAdapter.Mode.SELECTION_MODE){
            adapter.cancelSelection();
            fab.show();
        } else {
            super.onBackPressed();
        }
    }

    public void showSnackBar(int i){
        Snackbar.make(coordinatorLayout, i + " заметкок добавлено в корзину!", Snackbar.LENGTH_SHORT)
                .setAction("Отмена", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(coordinatorLayout,"Отменено! или нет...", Snackbar.LENGTH_LONG)
                                .show();
                    }
                })
                .show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("TAG", "        onOptionsItemSelected  onOptionsItemSelected  onOptionsItemSelected");

        if(toggle.onOptionsItemSelected(item))
            return true;

        hideFabs();

        switch (item.getItemId()){
            case R.id.delete_selection_items:
                Log.d("TAG", "       Adapter --- delete_selection_items");
                showSnackBar(adapter.getSelectedTasksCounter());
                adapter.deleteSelectedTasks();
                break;
            case R.id.select_item:
                Log.d("TAG", "       Adapter --- set selection mode");
                toolbar.setVisibility(View.GONE);
                toolbar2.setVisibility(View.VISIBLE);
                adapter.setSelectionMode(MainRecyclerAdapter.Mode.SELECTION_MODE);
                setSupportActionBar(toolbar2);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TAG", "       TOOLBAR 2 click");
                        adapter.cancelSelection();
                        // toggle.setDrawerIndicatorEnabled(true);
                    }
                });
                fab.hide();
                break;
            case R.id.green:
                adapter.setColorSelectionTasks(Constants.GREEN);
                fab.show();
                break;
            case R.id.red:
                adapter.setColorSelectionTasks(Constants.RED);
                fab.show();
                break;
            case R.id.blue:
                adapter.setColorSelectionTasks(Constants.BLUE);
                fab.show();
                break;
            case R.id.yellow:
                adapter.setColorSelectionTasks(Constants.YELLOW);
                fab.show();
                break;
            case R.id.white:
                adapter.setColorSelectionTasks(0);
                break;
            case R.id.change_view:

                String s = sharedPreferences.getString(Constants.MAIN_RECYCLER_LAYOUT, Constants.LAYOUT_LIST);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                RecyclerView.LayoutManager layoutManager;

                if(s.equals(Constants.LAYOUT_LIST)) {
                    layoutManager = new StaggeredGridLayoutManager(2, 1);
                    editor.putString(Constants.MAIN_RECYCLER_LAYOUT, Constants.LAYOUT_GRID);

                } else {
                    layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    editor.putString(Constants.MAIN_RECYCLER_LAYOUT, Constants.LAYOUT_LIST);
                }
                editor.commit();
                recyclerView.setLayoutManager(layoutManager);
                adapter.notifyDataSetChanged();
                break;
            case R.id.delete_forever:
                Log.d("TAG", "      Main3Activity           RRR delete_forever");

                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                // alert.setTitle("Очистить корзину?");
                alert.setMessage("Вы действительно хотите удалить выделенные заметки из корзины навсегда?");

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.deleteSelectedTasksForever();
                        delateForever.setVisible(false);
                        Log.d("TAG", "                      CCCCCLLLLLLLLIIIIIIRRRRRRRRR ++++++");
                        clearBascet.setVisible(true);
                    }
                });
                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.show();
                break;
            case R.id.clear_basket:
                Log.d("TAG", "      Main3Activity           RRR CLEAR BASKET");
                final AlertDialog.Builder alert2 = new AlertDialog.Builder(this);
                alert2.setTitle("Очистить корзину?");
                alert2.setMessage("Вы действительно хотите удалить все заметки из корзины навсегда?");
                alert2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (SuperTask t:dbHelper.getTasks(Constants.DELETED)
                                ) {
                            dbHelper.deleteBook(t);
                        }
                        adapter.getTasks().clear();
                        adapter.notifyDataSetChanged();
                        Log.d("TAG", "                      CCCCCLLLLLLLLIIIIIIRRRRRRRRR ------clear_basket");
                        clearBascet.setVisible(false);
                    }
                });
                alert2.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert2.show();

                break;
            case R.id.cancel_selection:
                adapter.cancelSelection();
                fab.show();

        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        System.out.println(item.getTitle());
        // Handle navigation view item clicks here.
        hideFabs();
        int id = item.getItemId();
        System.out.println("ID = " + id);
        switch (item.getItemId()){
            case R.id.undefined:
                mainToolbarText.setText("");
                currentKind = Constants.UNDEFINED;
                values = dbHelper.getTasks(currentKind);
                adapter.dataChanged(values);
                delateForever.setVisible(false);
                fab.show();
                clearBascet.setVisible(false);
                Log.d("TAG", "                      CCCCCLLLLLLLLIIIIIIRRRRRRRRR ---------undefined");
                break;
            case R.id.deleted:
                mainToolbarText.setText("Корзина");
                currentKind = Constants.DELETED;
                values = dbHelper.getTasks(Constants.DELETED);
                checkOldTask();
                adapter.dataChanged(values);
                setColor.setVisible(false);
                delete.setVisible(false);
                clearBascet.setVisible(true);
                Log.d("TAG", "                      CCCCCLLLLLLLLIIIIIIRRRRRRRRR ++++++");
                fab.hide();
                break;
            case R.id.archive:
                mainToolbarText.setText("Архив");
                currentKind = Constants.ARCHIVE;
                values = dbHelper.getTasks(Constants.ARCHIVE);
                adapter.dataChanged(values);
                clearBascet.setVisible(false);

                break;
            case R.id.notifications:
                mainToolbarText.setText("Напоминания");
                //currentKind = Constants.NOTIFICATIONS;
                values = dbHelper.getNotificationTasks();
                adapter.dataChanged(values);
                break;

            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                break;
            case R.id.exit:
                this.finish();
                break;
        }

        if (id == R.id.add){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Добавить раздел");
            //alert.setMessage("Message");
            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            input.setBackgroundColor(0);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = String.valueOf(input.getText());
                    // Do something with value!
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    navigationView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("TAG", "                   NavigationView CLICK");
                        }
                    });
                    Section section = new Section();
                    section.setName(value);
                    dbHelper.addSection(section);
                    menu.clear();
                    onCreateOptionsMenu(menu);
          /*          Menu menu = navigationView.getMenu();
                    Menu submenu = menu.getItem(0).getSubMenu();
                    //MenuItem sections =  menu.getItem(R.id.sections);
                    submenu.add(R.id.sections,Menu.FIRST,Menu.NONE, value);*/

                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    private void checkOldTask() {
        long deletionPeriod = sharedPreferences.getLong("deletionPeriod", Constants.PERIOD_WEEK);
        for (SuperTask task : values
             ) {
            if(task.getDeletionTime() + deletionPeriod < System.currentTimeMillis())
                dbHelper.deleteBook(task);
        }
        values = dbHelper.getTasks(Constants.DELETED);
    }

    public void selectedItemCount(int selectedTasksCounter) {
        if(selectedTasksCounter == 0) {
            counterTextView.setVisibility(View.GONE);
            setColor.setVisible(false);
            delete.setVisible(false);
            choose.setVisible(true);
            setItemMovement(true);
            cancelSelection.setVisible(false);
            setSupportActionBar(toolbar);
            toolbar2.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            fab.show();
        } else {
            setItemMovement(false);
            counterTextView.setVisibility(View.VISIBLE);
            setColor.setVisible(true);
            delete.setVisible(true);
            choose.setVisible(false);
            counterTextView.setText(String.valueOf(selectedTasksCounter));
            cancelSelection.setVisible(true);
            if(currentKind.equals(Constants.DELETED)) {
                delateForever.setVisible(true);
                delete.setVisible(false);
                clearBascet.setVisible(false);
                Log.d("TAG", "                      CCCCCLLLLLLLLIIIIIIRRRRRRRRR --------selectedItemCount");
            }
        }
    }

    public void hideFabs(){
        Log.d("TAG", "      Main3Activity --- HIDE FABS  ---");
        if(!fabPressed)
            return;
        fab.startAnimation(fabCancelAnimation);
        fabAddST.startAnimation(fabClose);
        fabAddLT.startAnimation(fabClose);
        fabAddST.setClickable(false);
        fabAddLT.setClickable(false);
        fabPressed = false;
    }

    public void add(View v){
        if(fabPressed){
            hideFabs();
        } else {
            fab.startAnimation(fabAddAnimetion);
            fabAddST.startAnimation(fabOpen);
            fabAddLT.startAnimation(fabOpen);
            fabAddST.setClickable(true);
            fabAddLT.setClickable(true);
            fabPressed = true;
        }
    }

    public void newListTask(View v){
        hideFabs();
        Intent intent = new Intent(getApplicationContext(), ListTaskActivity.class);
        intent.putExtra("position", adapter.getTasks().size());
        startActivity(intent);
    }


    public void newSimpleTask(View v){
        hideFabs();
        Intent intent = new Intent(getApplicationContext(), SimpleTaskActivity.class);
        intent.putExtra("position", adapter.getTasks().size());
        startActivity(intent);
    }

    public void clearList(View v){
        Log.d("TAG", "      Main3Activity --- clearList  ---");
        dbHelper.clearDB();
        update();
    }

    private void initAnimation() {
        fabAddAnimetion = AnimationUtils.loadAnimation(this,R.anim.fab_add_rotation);
        fabCancelAnimation = AnimationUtils.loadAnimation(this,R.anim.fab_cancel_rotation);

        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);
    }


    public void setItemMovement(boolean b){
        ((SimpleItemTouchHelperCallback)callback).setCanMovement(b);
    }

    @Override
    protected void onPause() {
        Log.d("TAG", "      Activity --- onPause  ---");
        values = adapter.getTasks();
        // save because positions could change
        for (SuperTask s:values
                ) {
            if(!dbHelper.isRemind(s))
                s.setRemind(false);
            dbHelper.updateTask(s, currentKind);
        }
        super.onPause();
    }

    void update(){
        Log.d("TAG", "      Activity --- update  ---");
        values = dbHelper.getTasks(currentKind);
        if(adapter!=null)
            adapter.dataChanged(values);
    }


    @Override
    protected void onResume() {
        Log.d("TAG", "      Activity --- onResume  ---");

        update();
        super.onResume();
    }

    public String getCurrentKind() {
        return currentKind;
    }
}
