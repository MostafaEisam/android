package com.example.dong.secondapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dong on 2015-05-26.
 */

//모델 및 뷰객체와 상호동작하는 컨트롤러
//특정 범죄의 상세내역을 보여주고 사용자가 수정한 상세 내역을 변경하는 역할
public class CrimeFragment extends Fragment {
    public static final String EXTRA_CRIME_ID = "com.example.dong.secondapp.crime_id";

    private static final String TAG = "CrimeFragment";
    private static final String DIALOG_IMAGE = "image";

    public static final String DIALOG_DATE = "date";
    public static final String DIALOG_TIME = "time";
    public static final int REQUEST_DATE = 0;
    public static final int REQUEST_TIME = 1;
    public static final int REQUEST_SELECT = 2;
    public static final int REQUEST_PHOTO = 3;
    private static final int REQUEST_CONTACT = 4;

    private Crime crime;
    private EditText eT;
    private Button dateBtn;
    private CheckBox solvedCheckBox;
    private ImageButton mPhotoBtn;
    private ImageView mPhotoView;
    private Button mSuspectButton;
    private Callbacks mCallbacks;

    /*
    호스팅 액티비티에서 구현할 필요가 있는 인터페이스
     */
    public interface Callbacks{
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    //프래그먼트를 호스팅하는 어떤 액티비티에서도 호출되므로 public
    //프래그먼트는 자신의 상태(데이터)를 저장하거나 읽는 번들 객체를 가진다
    //Fragment.onSaveInstanceState(Bundle)오버라이드하여 사용
    //Fragment.onCreate()에서 프래그먼트의 뷰를 인플레이트x
    //프래그먼트 인스턴스는 onCreate()에서 구성

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);  //옵션메뉴의 콜백 메서드를 구현한다는 것을 FragmentManager에게 알린다.

        //getActivity()를 이용해 간단하게 CrimeActivity의 인텐트에 바로 액세스
        //UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        //프래그먼트가 자신에게 전달된 인자를 엑세스
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);

        crime = CrimeLab.get(getActivity()).getCrime(crimeId);
        //crime = new Crime();
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCirmes();
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();

        // 인자 번들을 프래그먼트에 첨부(단, 프래그먼트 생성 후 그리고 그 프래그먼트가 액티비티에 추가되기 전에)
        fragment.setArguments(args);

        return fragment;
    }

    //프래그먼트의 뷰는 onCreateView()에서 생성하고 구성
    //프래그먼트 뷰의 레이아웃을 인플레이트한 후 인플레이트 된 View를 호스팅 액티비티에게 반환하는 역할
    //LayoutInflater,ViewGroup은 레이아웃을 인플레이트하기 위해 필요
    //Bundle은 저장된 상태로부터 뷰를 재생성하기 위해 이 메소드가 사용할 수 있는 데이터를 갖는다.
    @TargetApi(11) // -> setDisplayHomeAsUpEnabled()호출에 필요
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //프래그먼트의 뷰를 명시적으로 인플레이트(R.layout.fragment_crime을 전달함으로써)
        //위젯들을 올바르게 구성하기 위해 필요한 뷰의 부모
        //인플레이트된 뷰를 뷰의 부모에게 추가할 것인지를 LayoutInflater에게 알려준다.
        //여기서는 false -> 인플레이트된 뷰를 호싕 액티비티의 코드에 추가 하기 때문
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            //메타 데이터에 지명된 부모가 없으면 캐럿 표시를 보여주지 않는다.
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
                appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        eT = (EditText) v.findViewById(R.id.crime_title);
        eT.setText(crime.getTitle());
        eT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
                mCallbacks.onCrimeUpdated(crime);
                getActivity().setTitle(crime.getTitle());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dateBtn = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        //dateBtn.setEnabled(false);  //버튼 비활성
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                //DatePickerFragment dialog = new DatePickerFragment();

//                //프래그먼트의 인자를 전달하기 위해 생성자를 newInstance()로 대체
//                DatePickerFragment dialog = DatePickerFragment.newInstance(crime.getDate());
//
//                //CrimeFragment를 DatePickerFragment로부터 사용자가 선택한 날짜를 돌려받으려면 두 프래그먼트 간의 관계를 추적 관리하는 방법이 필요
//                //액티비티의 경우 startActivityForResult()로 ActivityManager가 부모-자식 액티비티의 관계를 추적 관리
//                //따라서 자식 액티비티가 끝나면 메소드의 결과를 어떤 액티비티가 받아야 하는지 ActivityManager가 안다.
//                //CrimeFragment를 DatePickerFragment의 목표 프래그먼트(target fragment)로 만들면 이와 유사한 관계 생성가능
//                //public void setTargetFragment(Fragment fragment, int requestCode)
//                //요청 코드는 startActivityForResult()의 인자로 전달한 것과 같은 것
//                //FragmentManager는 목표 프래그먼트와 요청 코드를 추적관리한다.
//                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
//
//                //DialogFragment를 FragmentManager에 추가하여 화면에 나오게 하려면 다음의 프래그먼트 인스턴스 메소드를 호출
//                //public void show(FragmentManager fm, String tag)
//                //public void show(FragmentTransaction transaction, String tag)
//                //String인자는 FragmentManager의 리스트에서 DialogFragment를 고유하게 식별하는데 이용
//                //FragmentTransaction나 FragmentManager 중 어떤걸 사용할지는 우리에게 달렸다.
//                //FragmentManager를 쓰는 경우 트랜잭션이 자동으로 생성되어 커밋된다.
//                dialog.show(fm,DIALOG_DATE);

//                TimePickerFragment dialog = TimePickerFragment.newInstance(crime.getDate());
//
//                //dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
//                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
//
//                dialog.show(fm,DIALOG_TIME);

                SelectFragment dialog = SelectFragment.newInstance(crime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_SELECT);

                dialog.show(fm, DIALOG_DATE);
            }
        });

        solvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solve);
        solvedCheckBox.setChecked(crime.isSolved());
        solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //범죄 해결 여부 값을 설정
                crime.setSolved(isChecked);
                mCallbacks.onCrimeUpdated(crime);
            }
        });

        mPhotoBtn = (ImageButton) v.findViewById(R.id.crime_imageButton);
        mPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //카메라 엑티비티를 론칭한다.
                Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p = crime.getPhoto();
                if (p == null)
                    return;

                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
            }
        });

        //만일 카메라를 사용할 수 없으면 카메라 기능을 못 쓰도록 한다.
        PackageManager pm = getActivity().getPackageManager();
        boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD ||
                Camera.getNumberOfCameras() > 0;
        if (!hasACamera)
            mPhotoBtn.setEnabled(false);

        //암시적 인텐트를 생성하여 텍스트를 전송
        Button reportButton = (Button) v.findViewById(R.id.crime_reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));  //매번 선택기가 나타나도록 생성
                startActivity(intent);
            }
        });

        //안드로이드에게 연락처 요청하기
        //신들의 연락처로부터 용의자를 선택할 수 있게 해주는 암시적 인텐트 생성
        //이 암시적 인텐트는 액션 및 관련된 데이터를 찾을 수 있는 위치를 가질 것이다.
        //Intent.ACTION_PICK -> 연락처(contacts)DB에서 하나의 항목을 선택하는 것을 도와달라고 안드로이드에게 요청
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

                PackageManager pm = getActivity().getPackageManager();
                ////queryIntentActivities()로 인텐트를 전달, 전달된 인텐트에 응답할 수 있는 액티비티들의
                //메타데이터를 갖는 객체 List를 반환한다.
                List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
                boolean isIntentSafe = activities.size() > 0;

                if (isIntentSafe)
                    startActivityForResult(intent, REQUEST_CONTACT);
            }
        });

        if (crime.getSuspect() != null) {
            mSuspectButton.setText(crime.getSuspect());
        }

        return v;
    }

    private void showPhoto() {
        //우리 사진을 기준으로 이미지 버튼의 이미지를 설정 또는 재설정한다.
        Photo p = crime.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mPhotoView.setImageDrawable(b);
    }

    /*
    이미지 로딩은 onStart()에서, 해제는 onStop()에서 하는 것이 좋은 방법
    -> 이 메소드들은 우리 엑티비티가 사용자에게 보여질 수 있는 시점을 나타낸다.
    만일 이미지 로딩과 해제를 onResume(), onPause()에서 했다면 사용자에게 당혹스러운 결과가 되었을 것이다.

    일시중지(pause)된 엑티비티는 여전히 부분적으로 볼 수 있다. 예를 들어, 전체 화면을 덮고 있지 않는 엑티비티가
    제일 위에 열려 있을 때다. 만일 그런 경우에 onResume()과 onPause()를 사용했다면 이미지가 갑자기 사라졌다 다시
    나타날 것이다. 이미지의 로딩은 액티비티가 보이게 되는 즉시 하는 것이 좋다. 그리고 액티비티가 더 시상 볼 수 없다는
     것을 알 때 까지 해제를 기다리는 것이 좋다.
     */
    @Override
    public void onStart() {
        super.onStart();
        //CrimeFragment의 뷰가 사용자에게 보여지게 되는 즉시 사진이 준비되게 하기위해 onStart()에서 호출
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_SELECT) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            crime.setDate(date);
            mCallbacks.onCrimeUpdated(crime);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            //새로운 사진(Photo) 객체를 생성하고 그것을 범죄(crime) 데이터에 첨부 한다.
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                Log.i(TAG, "filename: " + filename);

                Photo p = new Photo(filename);
                crime.setPhoto(p);
                mCallbacks.onCrimeUpdated(crime);
                showPhoto();  //사용자가 CrimeCameraActivity에서 돌아왔을 때 이미지를 볼 수 있게 여기서도 호출
                Log.i(TAG, "Crime: " + crime.getTitle() + " has a photo");
            }
        } else if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();

            //값을 반환할 쿼리 필드를 지정한다.
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,  //연락처의 모든 표시명들을 요청
            };

            //쿼리를 수행한다. 여기서 contactUri는 SQL의 "where"절에 해당한다.
            //쿼리에서 반환된 결과 셋(result set)의 행(row)들을 읽거나 쓰는 데 필요한 Cursor(커서)객체를 얻는다.
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            //쿼리의 결과 데이터가 있는지 재확인한다.
            if (c.getCount() == 0) {
                c.close();
                return;
            }

            //첫번째 데이터 행(row)의 첫번째 열(column)을 추출한다.
            //그것이 용의자의 이름이다.
            c.moveToFirst();  //커서는 하나의 행만 포함하므로 첫 번째 행으로 이동한 후
            String suspect = c.getString(0);  //그 행의 첫번째 열의 값을 문자열로 받는다.
            String t1 = c.getString(1);
            crime.setSuspect(suspect);
            mCallbacks.onCrimeUpdated(crime);
            mSuspectButton.setText(suspect + t1);
            c.close();
        }


//        if(requestCode == REQUEST_DATE || requestCode == REQUEST_TIME){
//            Date date = (Date)data
//                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
//            crime.setDate(date);
//            updateDate();
//        }
    }

    private void updateDate() {
        //DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(getActivity().getApplication());
        //dateBtn.setText(dateFormat.format(crime.getDate()).toString());
        dateBtn.setText(crime.getDate().toString());
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (crime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, crime.getDate()).toString();

        String suspect = crime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, crime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //메타 데이터에 지정된 부모가 있는지 확인
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    //있으면 부모 액티비티로 이동
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    //있으면 부모 액티비티로 이동
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
