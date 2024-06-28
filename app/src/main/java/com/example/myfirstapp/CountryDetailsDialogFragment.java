//package com.example.myfirstapp;
//////
////public class CountryDetailsDialogFragment {
////}
//
//
//import static android.text.style.TtsSpan.ARG_COUNTRY_CODE;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.DialogFragment;
//import com.example.myfirstapp.CountryQuery;
//import com.example.myfirstapp.R;
//
//import java.util.List;
//
//public class CountryDetailsDialogFragment extends DialogFragment {
//
//    private static final String ARG_COUNTRY = "country";
//    private CountryQuery.Country country;
//
//    public static CountryDetailsDialogFragment newInstance(String countryCode, String countryName, String countryEmoji, String countryCapital) {
//        CountryDetailsDialogFragment fragment = new CountryDetailsDialogFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_COUNTRY_CODE, countryCode);
//        args.putString(ARG_COUNTRY_NAME, countryName);
//
//        args.putString(ARG_COUNTRY_CAPITAL, countryCapital);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        if (getArguments() != null) {
//            String countryCode = getArguments().getString(ARG_COUNTRY_CODE);
//            String countryName = getArguments().getString(ARG_COUNTRY_NAME);
//            //String countryEmoji = getArguments().getString(ARG_COUNTRY_EMOJI);
//            String countryCapital = getArguments().getString(ARG_COUNTRY_CAPITAL);
//            country = (CountryQuery.Country) getArguments().getSerializable(ARG_COUNTRY);
//        }
//        return new AlertDialog.Builder(requireContext())
//                .setTitle(country.name())
//                .setMessage(
//                        "\nCapital: " + country.capital() +
//                        "\nCurrency: " + country.currency() +
//                        "\nContinent: " + country.continent().name() +
//                        "\nLanguages: " + getLanguagesString(country.languages()))
//                .setPositiveButton(android.R.string.ok, null)
//                .create();
//    }
//
//    private String getLanguagesString(List<CountryQuery.Language> languages) {
//        StringBuilder languagesStr = new StringBuilder();
//        for (CountryQuery.Language language : languages) {
//            languagesStr.append(language.name()).append(", ");
//        }
//        return languagesStr.length() > 0 ? languagesStr.substring(0, languagesStr.length() - 2) : "";
//    }
//}
////import android.app.Dialog;
////import android.os.Bundle;
////import androidx.annotation.NonNull;
////import androidx.annotation.Nullable;
////import androidx.appcompat.app.AlertDialog;
////import androidx.fragment.app.DialogFragment;
////import com.google.gson.Gson;
////import java.util.List;
////
////public class CountryDetailsDialogFragment extends DialogFragment {
////
////    private static final String ARG_COUNTRY = "country";
////
////    private CountryWrapper country;
////
////    public static CountryDetailsDialogFragment newInstance(CountryQuery.Country country) {
////        CountryDetailsDialogFragment fragment = new CountryDetailsDialogFragment();
////        Gson gson = new Gson();
////        String countryJson = gson.toJson(new CountryWrapper(
////                country.getCode(),
////                country.getName(),
////                country.getEmoji(),
////                country.getCapital(),
////                country.getLanguages()
////                // Add other fields as needed
////        ));
////        Bundle args = new Bundle();
////        args.putString(ARG_COUNTRY, countryJson);
////        fragment.setArguments(args);
////        return fragment;
////    }
////
////    @NonNull
////    @Override
////    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
////        if (getArguments() != null) {
////            Gson gson = new Gson();
////            String countryJson = getArguments().getString(ARG_COUNTRY);
////            country = gson.fromJson(countryJson, CountryWrapper.class);
////        }
////
////        // Use country object to initialize dialog
////        AlertDialog dialog = new AlertDialog.Builder(requireContext())
////                .setTitle(country.getName())
////                .setMessage(
////                        "\nCapital: " + country.getCapital() +
////                                "\nCurrency: " + country.getCurrency() +
////                                "\nContinent: " + country.getContinent() +
////                                "\nLanguages: " + getLanguagesString(country.getLanguages()))
////                .setPositiveButton(android.R.string.ok, null)
////                .create();
////
////        return dialog;
////    }
////
////    private String getLanguagesString(List<String> languages) {
////        StringBuilder languagesStr = new StringBuilder();
////        for (String language : languages) {
////            languagesStr.append(language).append(", ");
////        }
////        if (languagesStr.length() > 0) {
////            languagesStr.delete(languagesStr.length() - 2, languagesStr.length()); // Remove last comma and space
////        }
////        return languagesStr.toString();
////    }
////}
