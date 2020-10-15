package com.fdt.kingcalculator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    private String[] titles;
    private ListView drawerList;
    private DrawerLayout mDrawerLayout;

    //点击时
    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    //打开时
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titles=getResources().getStringArray(R.array.titles);
        drawerList= findViewById(R.id.drawer);
        mDrawerLayout= findViewById(R.id.drawer_layout);
        drawerList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,titles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        if(savedInstanceState==null){
            int orientation=getResources().getConfiguration().orientation;
            if(orientation==1){
                selectItem(0);
            }else{
                selectItem(1);
            }
        }
    }


    //显示是
    private void selectItem(int position) {
        Fragment fragment;
        if(position==0){
            fragment=new Easy();
        }else {
            fragment=new scientific();
        }
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        getActionBar().setTitle(titles[position]);
        mDrawerLayout.closeDrawer(drawerList);
    }


    //添加动作条
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //点击动作条
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment;
        FragmentTransaction ft;
        switch (item.getItemId()){
            case R.id.easy:
                fragment=new Easy();
                ft=getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,fragment);
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                getActionBar().setTitle(titles[0]);
                mDrawerLayout.closeDrawer(drawerList);
                return true;
            case R.id.scientific:
                fragment=new scientific();
                ft=getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,fragment);
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                getActionBar().setTitle(titles[1]);
                mDrawerLayout.closeDrawer(drawerList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}