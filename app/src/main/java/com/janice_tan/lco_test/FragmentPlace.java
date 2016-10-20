package com.janice_tan.lco_test;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import static com.janice_tan.lco_test.DBHandler.ORDER_BY_ASC;

/**
 * Created by janice on 18/10/16.
 */

public class FragmentPlace extends ListFragment {

    DBHandler dbHandler;
    Toolbar toolbar;
    ArrayAdapter placesAdapter;
    int categoryId;
    String orderBy, direction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = (Toolbar) this.getActivity().findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);


        dbHandler = new DBHandler(this.getActivity());
        categoryId = DBHandler.CATEGORY_ALL;
        orderBy = DBHandler.KEY_PLACE_NAME;
        direction = ORDER_BY_ASC;

        ArrayList places = dbHandler.getAllPlaces(orderBy, direction, categoryId);
        loadListData(places);
    }

    private void loadListData(final ArrayList places) {

        /*placesAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, places) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                Place place = (Place) places.get(position);
                text1.setText(place.getName());
                text2.setText(((Category) place.getCategory()).getName());
                return view;
            }
        };
        setListAdapter(placesAdapter);*/

        placesAdapter = new PlacesArrayAdapter(this.getActivity(), 0, places);
        setListAdapter(placesAdapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_main, menu);

        final SearchManager searchManager = (SearchManager) this.getContext().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextChange(String newText) {

                if (TextUtils.isEmpty(newText)){
                    ArrayList places = dbHandler.getAllPlaces(DBHandler.KEY_PLACE_NAME, ORDER_BY_ASC, DBHandler.CATEGORY_ALL);
                    loadListData(places);
                }

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                searchPlaces(query);
                // closes the search keyboard:
                searchView.clearFocus();

                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {

                searchView.setIconifiedByDefault(true);
                searchView.clearFocus();

                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                return true;

            case R.id.action_sort:
                View menuItemView = getActivity().findViewById(R.id.action_sort);
                showSortByMenu(menuItemView);
                return true;

            case R.id.action_filter:
                View menuItemFilterView = getActivity().findViewById(R.id.action_filter);
                showFilterMenu(menuItemFilterView);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void searchPlaces(String keywords) {

        dbHandler = new DBHandler(this.getActivity());
        ArrayList places = dbHandler.findPlaces(keywords);
        loadListData(places);

        placesAdapter.notifyDataSetChanged();
    }

    private void updatePlaces(String orderBy, String direction, int categoryId) {

        this.orderBy = orderBy;
        this.direction = direction;

        dbHandler = new DBHandler(this.getActivity());
        ArrayList places = dbHandler.getAllPlaces(orderBy, direction, categoryId);
        loadListData(places);

        placesAdapter.notifyDataSetChanged();
    }


    private PopupMenu.OnMenuItemClickListener onSortMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.sort_alphabet_asc:
                    updatePlaces(DBHandler.KEY_PLACE_NAME, ORDER_BY_ASC, categoryId);
                    return true;

                case R.id.sort_alphabet_desc:
                    updatePlaces(DBHandler.KEY_PLACE_NAME, DBHandler.ORDER_BY_DESC, categoryId);
                    return true;

                case R.id.sort_date_asc:
                    updatePlaces(DBHandler.KEY_PLACE_UPDATED, ORDER_BY_ASC, categoryId);
                    return true;

                case R.id.sort_date_desc:
                    updatePlaces(DBHandler.KEY_PLACE_UPDATED, DBHandler.ORDER_BY_DESC, categoryId);
                    return true;

                case R.id.sort_rating_asc:
                    updatePlaces(DBHandler.KEY_PLACE_RATING, ORDER_BY_ASC, categoryId);
                    return true;

                case R.id.sort_rating_desc:
                    updatePlaces(DBHandler.KEY_PLACE_RATING, DBHandler.ORDER_BY_DESC, categoryId);
                    return true;

                default:
                    return false;
            }
        }
    };

    public void showSortByMenu (View view) {
        PopupMenu sortOptions = new PopupMenu(this.getContext(), view, Gravity.RIGHT|Gravity.TOP);
        sortOptions.inflate(R.menu.menu_sort);
        sortOptions.setOnMenuItemClickListener(onSortMenuItemClickListener);
        sortOptions.show();
    }

    private PopupMenu.OnMenuItemClickListener onFilterMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            categoryId = item.getItemId();
            updatePlaces(orderBy, direction, categoryId);
            return true;
        }
    };

    public void showFilterMenu(View view) {
        PopupMenu filterMenu = new PopupMenu(this.getContext(), view, Gravity.RIGHT|Gravity.TOP);

        ArrayList<Category> categoryList = dbHandler.getAllCategories();

        filterMenu.getMenu().add(Menu.NONE, -1, Menu.NONE, "All");

        for(int i = 0; i < categoryList.size(); i ++) {
            Category category = categoryList.get(i);
            filterMenu.getMenu().add(Menu.NONE, category.getId(), Menu.NONE, category.getName());
        }

        filterMenu.setOnMenuItemClickListener(onFilterMenuItemClickListener);
        filterMenu.show();
    }

}
