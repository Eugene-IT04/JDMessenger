package com.jdcompany.jdmessenger.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.database.AppDatabase;
import com.jdcompany.jdmessenger.database.daos.MessageDao;
import com.jdcompany.jdmessenger.database.daos.UserDao;
import com.jdcompany.jdmessenger.ui.adapters.UsersAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainScreenFragment extends BaseFragment implements View.OnClickListener {

    Button btnFindNewUser;
    UsersAdapter usersAdapter = new UsersAdapter();
    UserDao userDao;
    MessageDao messageDao;
    Toolbar tbMainScreen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rvUsers);
        recyclerView.setAdapter(usersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        registerForContextMenu(recyclerView);


        tbMainScreen = view.findViewById(R.id.tbMainScreen);
        tbMainScreen.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isUpdate", true);
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_mainScreenFragment_to_registerUserFragment, bundle);
        });
        btnFindNewUser = view.findViewById(R.id.btnFindNewUser);
        btnFindNewUser.setOnClickListener(this);

        userDao = AppDatabase.getInstance(null).userDao();
        messageDao = AppDatabase.getInstance(null).messageDao();

        //after daos init
        createObservers();

        compositeDisposable = new CompositeDisposable();

        usersAdapter.setOnItemClickListener(userModel -> {
            Bundle bundle = new Bundle();
            bundle.putLong("userId", userModel.getId());
            bundle.putString("userName", userModel.getName());
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_mainScreenFragment_to_chatFragment, bundle);
        });
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = usersAdapter.getPosition();
        switch (item.getItemId()) {
            case R.id.optionDeleteUser:
                userDao.delete(usersAdapter.getData().get(position))
                        .subscribeOn(Schedulers.io())
                        .subscribe();
                break;
            case R.id.optionClearChat:
                messageDao.deleteAllMessagesByKey(usersAdapter.getData().get(position).getId() + InfoLoader.getInstance().getCurrentUser().getId())
                        .subscribeOn(Schedulers.io())
                        .subscribe();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_mainScreenFragment_to_findUserFragment);
    }

    void createObservers() {
        compositeDisposable.add(userDao
                .getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list ->
                    usersAdapter.setUsersCollection(list), e -> {
                }));
        compositeDisposable.add(messageDao
                .getLastMessagesForAllChats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list ->
                        usersAdapter.setLastMessagesCollection(list), e -> {}));
    }
}
