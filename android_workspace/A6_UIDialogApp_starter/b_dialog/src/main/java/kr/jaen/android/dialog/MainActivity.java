package kr.jaen.android.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import kr.jaen.android.dialog.databinding.ActivityMainBinding;
import kr.jaen.android.dialog.databinding.DialogTextEntryBinding;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnYesNoMessageDialog.setOnClickListener(view -> {
            createDialog(DialogType.YES_NO_MESSAGE).show();
        });

        binding.btnYesNoLongMessageDialog.setOnClickListener(view -> {
            createDialog(DialogType.YES_NO_LONG_MESSAGE).show();
        });

        binding.btnListDialog.setOnClickListener(view -> {
            createDialog(DialogType.LIST).show();
        });

        binding.btnProgressDialog.setOnClickListener(view -> {
            createDialog(DialogType.PROGRESS).show();

            progress = 0;
            progressDialog.setProgress(0);
            progressHandler.sendEmptyMessage(0);
        });

        binding.btnSingleChoiceDialog.setOnClickListener(view -> {
            createDialog(DialogType.SINGLE_CHOICE).show();
        });

        binding.btnMultipleChoiceDialog.setOnClickListener(view -> {
            createDialog(DialogType.MULTIPLE_CHOICE).show();
        });

        binding.btnTextEntryDialog.setOnClickListener(view -> {
            createDialog(DialogType.TEXT_ENTRY).show();
        });

        progressHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (progress >= 100) {
                    progressDialog.dismiss();
                }
                else {
                    progress++;
                    progressDialog.incrementProgressBy(1);
                    progressHandler.sendEmptyMessageDelayed(0, 100);
                }
            }
        };
    }


    private enum DialogType {
        YES_NO_MESSAGE,
        YES_NO_LONG_MESSAGE,
        LIST,
        PROGRESS,
        SINGLE_CHOICE,
        MULTIPLE_CHOICE,
        TEXT_ENTRY
    }

    private int progress;
    private ProgressDialog progressDialog;
    private Handler progressHandler;

    private Dialog createDialog(DialogType type) {
        switch (type) {
            case YES_NO_MESSAGE:
                return new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.icon_alert)
                        .setTitle("예 아니오 다이얼로그")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "예 선택 " + which,
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("아니오", (dialog, which) -> {
                            Toast.makeText(MainActivity.this, "아니오 선택 " + which,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .create();

            case YES_NO_LONG_MESSAGE:
                return new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.baseline_warning_amber_24)
                        .setTitle("예 아니오 다이얼로그")
                        .setMessage("동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세 무궁화 삼천리 화려강산")
                        .setPositiveButton("예", (dialog, which) -> {
                            Toast.makeText(MainActivity.this, "예 선택 " + which,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setNeutralButton("또 다른 버튼", (dialog, which) -> {
                            Toast.makeText(MainActivity.this, "또 다른 버튼 선택 " + which,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("아니오", (dialog, which) -> {
                            Toast.makeText(MainActivity.this, "아니오 선택 " + which,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .create();

            case LIST:
                String[] items = { "항목 1", "항목 2", "항목 3", "항목 4" };
                return new AlertDialog.Builder(MainActivity.this)
                        .setTitle("목록 다이얼로그")
                        .setItems(items, (dialog, which) -> {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("You selected: " + which + ", " + items[which])
                                    .show();
                        })
                        .create();

            case PROGRESS:
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setIcon(R.drawable.icon_alert);
                progressDialog.setTitle("프로그레스 다이얼로그");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(100);
                progressDialog.setIndeterminate(false);
                progressDialog.setButton("닫기", (dialog, which) -> {
                    Toast.makeText(MainActivity.this, "닫기 선택 " + which,
                            Toast.LENGTH_SHORT).show();
                });
                progressDialog.setButton2("취소",  (dialog, which) -> {
                    Toast.makeText(MainActivity.this, "취소 선택 " + which,
                            Toast.LENGTH_SHORT).show();
                });
                return progressDialog;

            case SINGLE_CHOICE:
                String[] items2 = { "지도", "위성", "교통", "거리 뷰" };
                return new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.icon_alert)
                        .setTitle("단일 선택 다이얼로그")
                        .setSingleChoiceItems(items2, 0, (dialog, which) -> {
                            Toast.makeText(MainActivity.this, "You selected: " + which + ", " + items2[which],
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setPositiveButton("예", (dialog, which) -> {
                            Toast.makeText(MainActivity.this, "예 선택 " + which,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("아니오", (dialog, which) -> {
                            Toast.makeText(MainActivity.this, "아니오 선택 " + which,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .create();

            case MULTIPLE_CHOICE:
                String[] items3 = { "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" };
                return new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.icon_alert)
                        .setTitle("다중 선택 다이얼로그")
                        .setMultiChoiceItems(items3,
                                new boolean[]{false, true, false, true, false, false, false},
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        Toast.makeText(MainActivity.this,
                                                "You selected: " + which + ", " + items3[which] + ", " + isChecked,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setPositiveButton("예", (dialog, which) -> {
                            Toast.makeText(MainActivity.this, "예 선택 " + which,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("아니오", (dialog, which) -> {
                            Toast.makeText(MainActivity.this, "아니오 선택 " + which,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .create();

            case TEXT_ENTRY:
                DialogTextEntryBinding binding = DialogTextEntryBinding.inflate(getLayoutInflater());
                return new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.icon_alert)
                        .setTitle("사용자정의 다이얼로그")
                        .setView(binding.getRoot())
                        .setPositiveButton("예", (dialog, which) -> {
                            Toast.makeText(MainActivity.this,
                                    "예 선택 " + which + ", " + binding.etUsername.getText() + ", " + binding.etPassword.getText(),
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("아니오", (dialog, which) -> {
                            Toast.makeText(MainActivity.this, "아니오 선택 " + which,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .create();
        }

        return null;
    }

}