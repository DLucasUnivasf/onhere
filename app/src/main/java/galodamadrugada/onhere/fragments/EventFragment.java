package galodamadrugada.onhere.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;


import galodamadrugada.onhere.domain.Event;

/**
 * Created by UNIVASF on 30/03/2017.
 */

/*
public class EventFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView mRecyclerView;
    private List<Event> mList;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                EventAdapter adapter = (EventAdapter) mRecyclerView.getAdapter();

                if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    List<Event> listAux = ((MainActivity) getActivity()).getSetCarList(10);

                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size());
                    }
                }
            }
        });


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);


        mList = ((MainActivity) getActivity()).getSetCarList(10);
        EventAdapter adapter = new EventAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter( adapter );


        return view;
    }

    @Override
    public void onClickListener(View view, int position) {
        Toast.makeText(getActivity(), "Position: "+position, Toast.LENGTH_SHORT).show();

        EventAdapter adapter = (EventAdapter) mRecyclerView.getAdapter();
        adapter.removeListItem(position);
    }
}
*/
