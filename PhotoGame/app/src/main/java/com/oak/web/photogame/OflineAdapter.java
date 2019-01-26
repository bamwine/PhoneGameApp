package com.oak.web.photogame;
import java.io.ByteArrayInputStream;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public  class OflineAdapter extends RecyclerView.Adapter<OflineAdapter.MyViewHolder> {

	private Context mContext;
	private List<Photoclass> photoclassList;

	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView title, downs,ups,content;
		public ImageView image;

		public MyViewHolder(View view) {
			super(view);

			title = (TextView) view.findViewById(R.id.card_title);
			content = (TextView) view.findViewById(R.id.card_content);
			ups = (TextView) view.findViewById(R.id.action_upvote);
			downs = (TextView) view.findViewById(R.id.action_downvote);
			image = (ImageView) view.findViewById(R.id.card_image);


		}
	}


	public OflineAdapter(Context mContext, List<Photoclass> photoclassList) {
		this.mContext = mContext;
		this.photoclassList = photoclassList;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.inflator, parent, false);

		return new MyViewHolder(itemView);
	}



	@Override
	public void onBindViewHolder(final MyViewHolder holder, int position) {
		Photoclass photoclass = photoclassList.get(position);

		//uri = Uri.parse(Constant.GET_SAVE_IMAGES+photoclass.getFilename());
		holder.title.setText(photoclass.getTitle());
		holder.content.setText(photoclass.getDescption());
		holder.ups.setText(photoclass.getUpvotes()); ;
		holder.downs.setText(photoclass.getDownvotes());
//holder.image.setImageURI(uri);
//
//		Picasso.get()
//				.load(Constant.GET_SAVE_IMAGES+photoclass.getFilename())
//				.placeholder(R.drawable.logo)
//				.error(R.drawable.logo)
//				.into(holder.image);

		byte[] outImage=photoclass.filename;
		ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
		Bitmap theImage = BitmapFactory.decodeStream(imageStream);
		holder.image.setImageBitmap(theImage);



	}





	@Override
	public int getItemCount() {
		return photoclassList.size();
	}
}

