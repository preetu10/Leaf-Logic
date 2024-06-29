//
//package com.example.myfirstapp;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.apollographql.apollo.ApolloClient;
//import com.apollographql.apollo.api.Response;
//import com.apollographql.apollo.exception.ApolloException;
//import com.apollographql.apollo.rx2.Rx2Apollo;
//import com.example.myfirstapp.CountriesQuery;
//import com.example.myfirstapp.CountryQuery;
//import com.example.myfirstapp.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.OkHttpClient;
//
//public class CountryActivity extends AppCompatActivity {
//
//    private ApolloClient apolloClient;
//    private RecyclerView recyclerView;
//    private CountryAdapter countryAdapter;
//    private List<CountriesQuery.Country> countryList = new ArrayList<>();
//    private CompositeDisposable compositeDisposable = new CompositeDisposable();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_country);
//
//        recyclerView = findViewById(R.id.recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        countryAdapter = new CountryAdapter(this, countryList, country -> {
//            fetchCountryDetails(country.code());
//        });
//        recyclerView.setAdapter(countryAdapter);
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//
//        apolloClient = ApolloClient.builder()
//                .serverUrl("https://countries.trevorblades.com/graphql")
//                .okHttpClient(okHttpClient)
//                .build();
//
//        fetchCountries();
//    }
//
//    private void fetchCountries() {
//        CountriesQuery countriesQuery = CountriesQuery.builder().build();
//        compositeDisposable.add(
//                Rx2Apollo.from(apolloClient.query(countriesQuery))
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(Schedulers.io())
//                        .subscribe(response -> {
//                            runOnUiThread(() -> {
//                                countryList.clear();
//                                countryList.addAll(response.data().countries());
//                                countryAdapter.notifyDataSetChanged();
//                            });
//                        }, throwable -> runOnUiThread(() ->
//                                Toast.makeText(CountryActivity.this, "Error fetching countries", Toast.LENGTH_SHORT).show()))
//        );
//    }
//
//    private void fetchCountryDetails(String countryCode) {
//        CountryQuery countryDetailsQuery = CountryQuery.builder()
//                .country_code(countryCode)
//                .build();
//        compositeDisposable.add(
//                Rx2Apollo.from(apolloClient.query(countryDetailsQuery))
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(Schedulers.io())
//                        .subscribe(response -> {
//                            runOnUiThread(() -> {
//                                showCountryDetails(response.data().country());
//                            });
//                        }, throwable -> runOnUiThread(() ->
//                                Toast.makeText(CountryActivity.this, "Error fetching country details", Toast.LENGTH_SHORT).show()))
//        );
//    }
//
//    private void showCountryDetails(CountryQuery.Country country) {
//        Toast.makeText(CountryActivity.this, country.capital, Toast.LENGTH_SHORT).show();
////        CountryDetailsDialogFragment dialogFragment = CountryDetailsDialogFragment.newInstance(country);
////        dialogFragment.show(getSupportFragmentManager(), "country_details");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        compositeDisposable.dispose();
//    }
//}

package com.example.myfirstapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.example.myfirstapp.CountriesQuery;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class CountryActivity extends AppCompatActivity {

    private ApolloClient apolloClient;
    private RecyclerView recyclerView;
    private CountryAdapter countryAdapter;
    private List<CountriesQuery.Country> countryList = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        countryAdapter = new CountryAdapter(this, countryList);
        recyclerView.setAdapter(countryAdapter);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        apolloClient = ApolloClient.builder()
                .serverUrl("https://countries.trevorblades.com/graphql")
                .okHttpClient(okHttpClient)
                .build();

        fetchCountries();
    }

    private void fetchCountries() {
        CountriesQuery countriesQuery = CountriesQuery.builder().build();
        compositeDisposable.add(
                Rx2Apollo.from(apolloClient.query(countriesQuery))
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(response -> {
                            runOnUiThread(() -> {
                                countryList.clear();
                                countryList.addAll(response.data().countries());
                                countryAdapter.notifyDataSetChanged();
                            });
                        }, throwable -> runOnUiThread(() ->
                                Toast.makeText(CountryActivity.this, "Error fetching countries", Toast.LENGTH_SHORT).show()))
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
