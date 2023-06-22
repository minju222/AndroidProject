package kr.jaen.android.listview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ArrayAdapterListViewHolderActivity extends AppCompatActivity {

    private static final String TAG = "ListViewActivity_SCSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        // 1. 리스트 뷰 생성
        ListView listView = findViewById(R.id.listView);

        // 2. 어댑터 생성
        MyAdapter adapter = new MyAdapter(this, R.layout.row, COUNTRIES);

        // 3. 리스트 뷰와 어댑터 결합
        listView.setAdapter(adapter);
    }

    class MyAdapter extends ArrayAdapter<String> {
        int resource;
        Object [] objects;
        public MyAdapter(Context context, int resource, String[] objects) {
            super(context, resource,  objects);
            this.resource = resource;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder = null;

            if(view == null){
                LayoutInflater inf = (ArrayAdapterListViewHolderActivity.this).getLayoutInflater();
                view = inf.inflate(resource, null);
                Log.d(TAG, "getView: Inflated..");

                holder = new ViewHolder();
                holder.textView = view.findViewById(R.id.country_name);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }

            holder.textView.setText(objects[position].toString());

            view.setOnClickListener(view1 -> {
                Toast.makeText(ArrayAdapterListViewHolderActivity.this, objects[position] + " selected..", Toast.LENGTH_SHORT).show();
            });

            return view;
        }
    }


    class ViewHolder {
        TextView textView;




    }



    // 리스트 뷰에 표현할 데이터 준비
    private static final String[] COUNTRIES = new String[] { "Afghanistan", "Albania",
            "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
            "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia",
            "Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain",
            "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
            "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina",
            "Botswana", "Bouvet Island", "Brazil",
            "British Indian Ocean Territory", "British Virgin Islands",
            "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cote d'Ivoire",
            "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
            "Central African Republic", "Chad", "Chile", "China",
            "Christmas Island", "Cocos (Keeling) Islands", "Colombia",
            "Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia",
            "Cuba", "Cyprus", "Czech Republic",
            "Democratic Republic of the Congo", "Denmark", "Djibouti",
            "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt",
            "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
            "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji",
            "Finland", "Former Yugoslav Republic of Macedonia", "France",
            "French Guiana", "French Polynesia", "French Southern Territories",
            "Gabon", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
            "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala",
            "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
            "Heard Island and McDonald Islands", "Honduras", "Hong Kong",
            "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq",
            "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
            "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
            "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
            "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Madagascar",
            "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
            "Marshall Islands", "Martinique", "Mauritania", "Mauritius",
            "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
            "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
            "Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
            "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria",
            "Niue", "Norfolk Island", "North Korea", "Northern Marianas",
            "Norway", "Oman", "Pakistan", "Palau", "Panama",
            "Papua New Guinea", "Paraguay", "Peru", "Philippines",
            "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
            "Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe",
            "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia",
            "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines",
            "Samoa", "San Marino", "Saudi Arabia", "Senegal", "Seychelles",
            "Sierra Leone", "Singapore", "Slovakia", "Slovenia",
            "Solomon Islands", "Somalia", "South Africa",
            "South Georgia and the South Sandwich Islands", "South Korea",
            "Spain", "Sri Lanka", "Sudan", "Suriname",
            "Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland",
            "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
            "The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga",
            "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
            "Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
            "Ukraine", "United Arab Emirates", "United Kingdom",
            "United States", "United States Minor Outlying Islands", "Uruguay",
            "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam",
            "Wallis and Futuna", "Western Sahara", "Yemen", "Yugoslavia",
            "Zambia", "Zimbabwe" };
}