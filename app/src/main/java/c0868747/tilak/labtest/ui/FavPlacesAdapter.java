package c0868747.tilak.labtest.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import c0868747.tilak.labtest.R;
import c0868747.tilak.labtest.databinding.ItemMyFavPlacesBinding;
import c0868747.tilak.labtest.model.FavLocation;
import c0868747.tilak.labtest.model.PlaceType;

public class FavPlacesAdapter extends RecyclerView.Adapter<FavPlacesAdapter.VH> {

    final private Context context;
    private FavPlaceListener listener;
    private ArrayList<FavLocation> locations = new ArrayList<>();
    final String home = PlaceType.HOME.toString();
    final String work = PlaceType.WORK.toString();

    public FavPlacesAdapter(Context context,FavPlaceListener listener,ArrayList<FavLocation> locations){
        this.context = context;
        this.listener = listener;
        this.locations = locations;
    }

    public void updateData(List<FavLocation> locations){
        this.locations = (ArrayList<FavLocation>) locations;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyFavPlacesBinding binding = ItemMyFavPlacesBinding.inflate(
                LayoutInflater.from(context),parent,false
        );
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(locations.get(position),position,listener);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        ItemMyFavPlacesBinding binding;
        public VH(ItemMyFavPlacesBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(FavLocation model,int position,FavPlaceListener listener){
            binding.title.setText(model.getTitle());
            binding.desc.setText(model.getDescription());

            binding.container.setOnClickListener(v -> {
                listener.viewLocation(model,position);
            });
            binding.info.setOnClickListener( v -> {
                listener.delete(model,position);
            });

            if(model.getTitle().equals(home)){
                binding.image.setImageResource(R.drawable.ic_baseline_home_24);
            }
            else if(model.getTitle().equals(work)){
                binding.image.setImageResource(R.drawable.ic_baseline_work_24);
            }
            else{
                binding.image.setImageResource(R.drawable.ic_baseline_location_on_24);
            }
        }
    }


}
