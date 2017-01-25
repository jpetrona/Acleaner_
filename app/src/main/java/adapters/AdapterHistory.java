package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.the.bestna.cleaner.R;

import java.util.ArrayList;

import helpers.EntryItem;
import helpers.Item;
import helpers.SectionItem;

public class AdapterHistory extends ArrayAdapter<Item> {
    private ArrayList<Item> items;
    private LayoutInflater vi;
    private Context context;

    public AdapterHistory(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Item i = items.get(position);
        Typeface typeface_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        Typeface typeface_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        if (i != null) {
            if (i.isSection()) {
                SectionItem si = (SectionItem) i;
                v = vi.inflate(R.layout.item_list_divisor, null);
                v.setBackgroundColor(context.getResources().getColor(R.color.bg_color));
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);

                final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
                sectionView.setTypeface(typeface_bold);
                sectionView.setTextColor(context.getResources().getColor(R.color.white_color));
                sectionView.setText(si.getTitle());

            } else {
                final EntryItem ei = (EntryItem) i;
                v = vi.inflate(R.layout.item_list_history, null);
                final ImageView icono = (ImageView) v.findViewById(R.id.list_item_entry_drawable);
                final TextView title = (TextView) v.findViewById(R.id.list_item_entry_title);
                final TextView cantidad = (TextView) v.findViewById(R.id.list_item_entry_summary);
                final CheckBox check = (CheckBox) v.findViewById(R.id.checkBox_ram);
                title.setTypeface(typeface_regular);
                cantidad.setTypeface(typeface);
                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // Log.d("Metodo ApplicationAdapter------>", ei.title);
                        ei.setSelected(buttonView.isChecked());
                    }
                });

                title.setText(ei.title);
                cantidad.setText(ei.cantidad);
                icono.setImageResource(ei.icono);
                check.setChecked(ei.isSelected());
            }
        }

        return v;
    }
}
