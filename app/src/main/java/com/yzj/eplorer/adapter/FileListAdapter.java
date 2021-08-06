package com.yzj.eplorer.adapter;
import android.graphics.drawable.PictureDrawable;
import android.view.View;
import android.widget.ImageView;
import com.caverock.androidsvg.SVG;
import com.yzj.base.BaseAdapter;
import com.yzj.base.BaseViewHolder;
import com.yzj.eplorer.R;
import com.yzj.eplorer.bean.FileItem;
import com.yzj.utils.MimeType;
import java.util.List;
import com.yzj.utils.IOUtil;
import java.io.FileInputStream;

public class FileListAdapter extends  BaseAdapter<FileItem,BaseViewHolder> {

	public FileListAdapter() {
		super(R.layout.item_file_list);
	}


	public FileListAdapter(List<FileItem> datas) {
		super(R.layout.item_file_list, datas);
	}

	@Override
	public void convert(final BaseViewHolder holder, FileItem item)  {

		String path=item.path;
		String name=item.name;
		String info=item.info;
		int icon=item.icon;
		if (holder.getLayoutPosition() == 0) {
			name = "...";
			info = "";
			icon = R.raw.file_icon_dir;
		}
		holder.setText(R.id.filename, name)
			.setText(R.id.fileinfo, info)
			.setImageWithSvg(R.id.fileicon, icon);

		
		//图片,其他特殊格式单独处理
		if (MimeType. isImageFile(path) || MimeType.isVideoFile(path)) 
			holder.setImageWithGlide(R.id.fileicon, path);
		if (MimeType.isCodeFile(path)) 
			holder.setImageWithSvg(R.id.fileicon, R.raw.file_icon_program);
		if(item.getExt().equals("svg"))
			holder.setImageWithSvg(R.id.fileicon,item.path);
		
		
	}

	@Override
	public void setData(List<FileItem> datas) {
		datas.add(0, new FileItem());
		super.setData(datas);
	}

}
