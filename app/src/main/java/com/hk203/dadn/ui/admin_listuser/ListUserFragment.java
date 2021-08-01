package com.hk203.dadn.ui.admin_listuser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hk203.dadn.MainActivity;
import com.hk203.dadn.databinding.FragmentListUserBinding;
import com.hk203.dadn.R;
import com.hk203.dadn.models.AdminUserProfile;
import com.hk203.dadn.viewmodels.AdminListUserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListUserFragment extends Fragment {
    private FragmentListUserBinding binding;
    private AdminListUserViewModel viewModel;
    public ListUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ListUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListUserFragment newInstance() {
        ListUserFragment fragment = new ListUserFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListUserBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AdminListUserViewModel.class);

        viewModel.getUserList().observe(getViewLifecycleOwner(), list ->{
            ListUserAdapter adapter = new ListUserAdapter(getContext(), R.layout.bac_sy_dieu_tri, list);
            binding.listUser.setAdapter(adapter);

        });

        viewModel.loadUserList(
                ((MainActivity)getActivity()).getAuthToken(),
                () -> binding.listLoading.post( () -> binding.listLoading.setText("Failed to load list")),
                () -> binding.listLoading.post(() -> binding.listLoading.setVisibility(View.GONE))
        );

        binding.listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdminUserProfile profile = (AdminUserProfile)parent.getItemAtPosition(position);
                FragmentManager fm = getParentFragmentManager();
                Fragment f = AdminUserDetailFragment.newInstance(profile);

                fm.beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, f, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return binding.getRoot();
    }
}