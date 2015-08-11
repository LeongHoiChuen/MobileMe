package com.example.jiabaotan2012.cw;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class MainActivity extends Activity {

	// Declaring our tabs and the corresponding fragments.
	ActionBar.Tab bmwTab, fordTab, toyotaTab;
	Fragment bmwFragmentTab = new BmwFragmentTab();
	Fragment toyotaFragmentTab = new ToyotaFragmentTab();
	Fragment fordFragmentTab = new FordFragmentTab();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Asking for the default ActionBar element that our platform supports.
		ActionBar actionBar = getActionBar();
		 
        // Screen handling while hiding ActionBar icon.
        actionBar.setDisplayShowHomeEnabled(false);
 
        // Screen handling while hiding Actionbar title.
        actionBar.setDisplayShowTitleEnabled(false);
 
        // Creating ActionBar tabs.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        // Setting custom tab icons.
        bmwTab = actionBar.newTab().setIcon(R.drawable.bmw_logo);
        toyotaTab = actionBar.newTab().setIcon(R.drawable.toyota_logo);
        fordTab = actionBar.newTab().setIcon(R.drawable.ford_logo);
        
        // Setting tab listeners.
        bmwTab.setTabListener(new TabListener(bmwFragmentTab));
        toyotaTab.setTabListener(new TabListener(toyotaFragmentTab));
        fordTab.setTabListener(new TabListener(fordFragmentTab));
       
        // Adding tabs to the ActionBar.
        actionBar.addTab(bmwTab);
        actionBar.addTab(toyotaTab);
        actionBar.addTab(fordTab);
	}
}
