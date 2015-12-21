package com.malpo.bheard.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malpo.bheard.R;
import com.malpo.bheard.models.Artist;

import java.util.ArrayList;
import java.util.List;

// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private RecyclerView rv;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);

        List<Artist> artists = new ArrayList<>();
        artists.add(new Artist("Wilco"));
        artists.add(new Artist("Sufjan"));
        artists.add(new Artist("Devendra"));
        artists.add(new Artist("Bob Dylan"));
        artists.add(new Artist("Beatles"));
        artists.add(new Artist("Blah"));
        artists.add(new Artist("Blah"));
        artists.add(new Artist("Blah"));
        artists.add(new Artist("Blah"));
        artists.add(new Artist("Blah"));
        artists.add(new Artist("Blah"));
        artists.add(new Artist("Blah"));
        artists.add(new Artist("Blah"));
        artists.add(new Artist("Blah"));
        artists.add(new Artist("Blah"));

        RVAdapter rvAdapter = new RVAdapter(artists);
        rv.setAdapter(rvAdapter);
    }
}