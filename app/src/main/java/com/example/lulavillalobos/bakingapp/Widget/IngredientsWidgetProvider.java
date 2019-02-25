package com.example.lulavillalobos.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.lulavillalobos.bakingapp.R;
import com.example.lulavillalobos.bakingapp.UI.MainActivity;

public class IngredientsWidgetProvider extends AppWidgetProvider {

    public static final String UPDATE_INGREDIENTS_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(UPDATE_INGREDIENTS_ACTION)) {
            int appWidgetIds[] = manager.getAppWidgetIds(new ComponentName(context, IngredientsWidgetProvider.class));
            Log.e("received", intent.getAction());
            manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_ingredients);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //update each of the app widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; i++) {
            // Set up the intent that starts the ListViewService, which will
            // provide the views for this collection.
            Intent intent = new Intent(context, IngredientsWidgetService.class);

            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);

            // Set up the remote object to use a RemoteViews adapter
            // This adapter connects to a RemoteViewsService through the specified intent
            // This is how you populate the data
            remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.list_view_ingredients, intent);

            // Trigger list view item click
            Intent startActivityIntent = new Intent(context, MainActivity.class);
            PendingIntent startActivityPendingIntent = PendingIntent
                    .getActivity(
                            context,
                            0,
                            startActivityIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            remoteViews.setPendingIntentTemplate(R.id.list_view_ingredients, startActivityPendingIntent);

            // The empty view is displayed when the collection has no items
            // It should be in the same layout used to instantiate the RemoteViews object above
            remoteViews.setEmptyView(R.id.list_view_ingredients, R.id.empty_view);

            //TODO: Do additional processing specific to this app widget

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
