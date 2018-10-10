package com.cdiscount.rnadsense;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Display;
import android.view.WindowManager;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.search.DynamicHeightSearchAdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.search.SearchAdRequest;
import com.google.android.gms.ads.search.SearchAdView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ReactAdSenseView extends ReactViewGroup {

    protected SearchAdView adView;

    String adUnitID;
    Boolean adTest;
    Integer number;
    String container;
    String lines;
    String width;
    String fontFamily;
    Integer fontSizeTitle;
    Integer fontSizeDescription;
    Integer fontSizeDomainLink;
    String colorTitleLink;
    String colorText;
    String colorDomainLink;
    String colorBackground;
    String colorAdBorder;
    String colorBorder;
    Boolean noTitleUnderline;
    Boolean longerHeadlines;
    Boolean detailedAttribution;
    String query;

    public String getAdUnitID() {
        return adUnitID;
    }

    public Boolean getAdTest() {
        return adTest;
    }

    public void setAdTest(Boolean adTest) {
        this.adTest = adTest;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public Integer getFontSizeTitle() {
        return fontSizeTitle;
    }

    public void setFontSizeTitle(Integer fontSizeTitle) {
        this.fontSizeTitle = fontSizeTitle;
    }

    public Integer getFontSizeDescription() {
        return fontSizeDescription;
    }

    public void setFontSizeDescription(Integer fontSizeDescription) {
        this.fontSizeDescription = fontSizeDescription;
    }

    public Integer getFontSizeDomainLink() {
        return fontSizeDomainLink;
    }

    public void setFontSizeDomainLink(Integer fontSizeDomainLink) {
        this.fontSizeDomainLink = fontSizeDomainLink;
    }

    public String getColorTitleLink() {
        return colorTitleLink;
    }

    public void setColorTitleLink(String colorTitleLink) {
        this.colorTitleLink = colorTitleLink;
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    public String getColorDomainLink() {
        return colorDomainLink;
    }

    public void setColorDomainLink(String colorDomainLink) {
        this.colorDomainLink = colorDomainLink;
    }

    public String getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(String colorBackground) {
        this.colorBackground = colorBackground;
    }

    public String getColorAdBorder() {
        return colorAdBorder;
    }

    public void setColorAdBorder(String colorAdBorder) {
        this.colorAdBorder = colorAdBorder;
    }

    public String getColorBorder() {
        return colorBorder;
    }

    public void setColorBorder(String colorBorder) {
        this.colorBorder = colorBorder;
    }

    public Boolean getNoTitleUnderline() {
        return noTitleUnderline;
    }

    public void setNoTitleUnderline(Boolean noTitleUnderline) {
        this.noTitleUnderline = noTitleUnderline;
    }

    public Boolean getLongerHeadlines() {
        return longerHeadlines;
    }

    public void setLongerHeadlines(Boolean longerHeadlines) {
        this.longerHeadlines = longerHeadlines;
    }

    public Boolean getDetailedAttribution() {
        return detailedAttribution;
    }

    public void setDetailedAttribution(Boolean detailedAttribution) {
        this.detailedAttribution = detailedAttribution;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ReactAdSenseView(final Context context) {
        super(context);
        this.createAdView();
    }

    private void createAdView() {
        if (this.adView != null) this.adView.destroy();

        final Context context = getContext();
        this.adView = new SearchAdView(context);
        this.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                int width = adView.getAdSize().getWidthInPixels(context);
                int height = adView.getAdSize().getHeightInPixels(context);
                int left = adView.getLeft();
                int top = adView.getTop();

                WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display display = mWindowManager.getDefaultDisplay();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                display.getMetrics(displaymetrics);
                adView.setAdSize(AdSize.SEARCH);
                adView.measure(width, height);
                adView.layout(left, top, left + width, top + height);
                sendOnSizeChangeEvent();
                sendEvent(RNAdSenseViewManager.EVENT_AD_LOADED, null);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                String errorMessage = "Unknown error";
                switch (errorCode) {
                    case SearchAdRequest.ERROR_CODE_INTERNAL_ERROR:
                        errorMessage = "Internal error, an invalid response was received from the ad server.";
                        break;
                    case SearchAdRequest.ERROR_CODE_INVALID_REQUEST:
                        errorMessage = "Invalid ad request, possibly an incorrect ad unit ID was given.";
                        break;
                    case SearchAdRequest.ERROR_CODE_NETWORK_ERROR:
                        errorMessage = "The ad request was unsuccessful due to network connectivity.";
                        break;
                    case SearchAdRequest.ERROR_CODE_NO_FILL:
                        errorMessage = "The ad request was successful, but no ad was returned due to lack of ad inventory.";
                        break;
                }
                WritableMap event = Arguments.createMap();
                WritableMap error = Arguments.createMap();
                error.putString("message", errorMessage);
                event.putMap("error", error);
                sendEvent(RNAdSenseViewManager.EVENT_AD_FAILED_TO_LOAD, event);
            }

            @Override
            public void onAdOpened() {
                sendEvent(RNAdSenseViewManager.EVENT_AD_OPENED, null);
            }

            @Override
            public void onAdClosed() {
                sendEvent(RNAdSenseViewManager.EVENT_AD_CLOSED, null);
            }

            @Override
            public void onAdLeftApplication() {
                sendEvent(RNAdSenseViewManager.EVENT_AD_LEFT_APPLICATION, null);
            }
        });
        this.addView(this.adView);
    }

    private void sendOnSizeChangeEvent() {
        int width;
        int height;
        ReactContext reactContext = (ReactContext) getContext();
        WritableMap event = Arguments.createMap();
        AdSize adSize = this.adView.getAdSize();
        if (adSize == AdSize.SMART_BANNER) {
            width = (int) PixelUtil.toDIPFromPixel(adSize.getWidthInPixels(reactContext));
            height = (int) PixelUtil.toDIPFromPixel(adSize.getHeightInPixels(reactContext));
        } else {
            width = adSize.getWidth();
            height = adSize.getHeight();
        }
        event.putDouble("width", width);
        event.putDouble("height", height);
        sendEvent(RNAdSenseViewManager.EVENT_SIZE_CHANGE, event);
    }

    private void sendEvent(String name, @Nullable WritableMap event) {
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        getId(),
                        name,
                        event);
    }

    public void loadBanner() {
        adView.setAdSize(AdSize.SEARCH);

        DynamicHeightSearchAdRequest.Builder builder =
            new DynamicHeightSearchAdRequest.Builder();

        builder.setAdTest(adTest);
        builder.setNumber(number);
        builder.setFontFamily(fontFamily);
        builder.setLongerHeadlines(longerHeadlines);
        builder.setDetailedAttribution(detailedAttribution);
        builder.setIsTitleUnderlined(noTitleUnderline);
        builder.setColorBorder(colorBorder);
        builder.setColorAdBorder(colorAdBorder);
        builder.setColorBackground(colorBackground);
        builder.setColorDomainLink(colorDomainLink);
        builder.setColorText(colorText);
        builder.setColorTitleLink(colorTitleLink);
        builder.setFontSizeDomainLink(fontSizeDomainLink);
        builder.setFontSizeDescription(fontSizeDescription);
        builder.setFontSizeTitle(fontSizeTitle);
        builder.setQuery(query);
        builder.setCssWidth(300);

        this.adView.loadAd(builder.build());
    }

    public void setAdUnitID(String adUnitID) {
        if (this.adUnitID != null) {
            // We can only set adUnitID once, so when it was previously set we have
            // to recreate the view
            this.createAdView();
        }
        this.adUnitID = adUnitID;
        this.adView.setAdUnitId(adUnitID);
    }

    /*@Override
    public void onAppEvent(String name, String info) {
        WritableMap event = Arguments.createMap();
        event.putString("name", name);
        event.putString("info", info);
        sendEvent(RNAdSenseViewManager.EVENT_APP_EVENT, event);
    }*/
}

public class RNAdSenseViewManager extends ViewGroupManager<ReactAdSenseView> {

    public static final String REACT_CLASS = "RNAdSenseView";

    public static final String PROP_AD_UNIT_ID = "adUnitID";
    public static final String PROP_AD_TEST = "adTest";

    public static final String PROP_NUMBER="number";
    public static final String PROP_CONTAINER="container";
    public static final String PROP_LINES="lines";
    public static final String PROP_WIDTH="width";
    public static final String PROP_FONTFAMILY="fontFamily";
    public static final String PROP_FONTSIZETITLE="fontSizeTitle";
    public static final String PROP_FONTSIZEDESCRIPTION="fontSizeDescription";
    public static final String PROP_FONTSIZEDOMAINLINK="fontSizeDomainLink";
    public static final String PROP_COLORTITLELINK="colorTitleLink";
    public static final String PROP_COLORTEXT="colorText";
    public static final String PROP_COLORDOMAINLINK="colorDomainLink";
    public static final String PROP_COLORBACKGROUND="colorBackground";
    public static final String PROP_COLORADBORDER="colorAdBorder";
    public static final String PROP_COLORBORDER="colorBorder";
    public static final String PROP_NOTITLEUNDERLINE="noTitleUnderline";
    public static final String PROP_LONGERHEADLINES= "longerHeadlines";
    public static final String PROP_DETAILATTRIBUTION="detailedAttribution";
    public static final String PROP_QUERY="query";

    public static final String EVENT_SIZE_CHANGE = "onSizeChange";
    public static final String EVENT_AD_LOADED = "onAdLoaded";
    public static final String EVENT_AD_FAILED_TO_LOAD = "onAdFailedToLoad";
    public static final String EVENT_AD_OPENED = "onAdOpened";
    public static final String EVENT_AD_CLOSED = "onAdClosed";
    public static final String EVENT_AD_LEFT_APPLICATION = "onAdLeftApplication";
    public static final String EVENT_APP_EVENT = "onAppEvent";

    public static final int COMMAND_LOAD_BANNER = 1;
    private int fixedWidth = 0;
    private int fixedHeight = 0;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected ReactAdSenseView createViewInstance(ThemedReactContext themedReactContext) {
        ReactAdSenseView adView = new ReactAdSenseView(themedReactContext);
        return adView;
    }

    @Override
    public void addView(ReactAdSenseView parent, View child, int index) {
        throw new RuntimeException("RNPublisherBannerView cannot have subviews");
    }

    @Override
    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        String[] events = {
            EVENT_SIZE_CHANGE,
            EVENT_AD_LOADED,
            EVENT_AD_FAILED_TO_LOAD,
            EVENT_AD_OPENED,
            EVENT_AD_CLOSED,
            EVENT_AD_LEFT_APPLICATION,
            EVENT_APP_EVENT
        };
        for (int i = 0; i < events.length; i++) {
            builder.put(events[i], MapBuilder.of("registrationName", events[i]));
        }
        return builder.build();
    }

    @ReactProp(name = PROP_AD_UNIT_ID)
    public void setPropAdUnitID(final ReactAdSenseView view, final String adUnitID) {
        view.setAdUnitID(adUnitID);
    }

    @ReactProp(name = PROP_AD_TEST)
    public void setPropAdTest(final ReactAdSenseView view, final String adTest) {
        view.setAdTest(Boolean.valueOf(adTest));
    }

    @ReactProp(name = PROP_NUMBER)
    public void setPropNumber(final ReactAdSenseView view, final String number) {
        view.setNumber(Integer.parseInt(number));
    }

    @ReactProp(name = PROP_CONTAINER)
    public void setPropContainer(final ReactAdSenseView view, final String container) {
        view.setContainer(container);
    }

    @ReactProp(name = PROP_LINES)
    public void setPropLines(final ReactAdSenseView view, final String lines) {
        view.setLines(lines);
    }

    @ReactProp(name = PROP_FONTFAMILY)
    public void setPropFontFamily(final ReactAdSenseView view, final String fontFamily) {
        view.setFontFamily(fontFamily);
    }

    @ReactProp(name = PROP_FONTSIZETITLE)
    public void setPropFontSizeTitle(final ReactAdSenseView view, final String fontSizeTitle) {
        view.setFontSizeTitle(Integer.parseInt(fontSizeTitle));
    }

    @ReactProp(name = PROP_FONTSIZEDESCRIPTION)
    public void setPropFontsizedescription(final ReactAdSenseView view, final String fontSizeDescription) {
        view.setFontSizeDescription(Integer.parseInt(fontSizeDescription));
    }

    @ReactProp(name = PROP_FONTSIZEDOMAINLINK)
    public void setPropFontsizedomainlink(final ReactAdSenseView view, final String fontSizeDomainLink) {
        view.setFontSizeDomainLink(Integer.parseInt(fontSizeDomainLink));
    }

    @ReactProp(name = PROP_COLORTITLELINK)
    public void setPropColorTitleLink(final ReactAdSenseView view, final String colorTitleLink) {
        view.setColorTitleLink(colorTitleLink);
    }

    @ReactProp(name = PROP_COLORTEXT)
    public void setPropColorText(final ReactAdSenseView view, final String colorText) {
        view.setColorTitleLink(colorText);
    }

    @ReactProp(name = PROP_COLORDOMAINLINK)
    public void setPropColorDomainLink(final ReactAdSenseView view, final String colorDomainLink) {
        view.setColorDomainLink(colorDomainLink);
    }

    @ReactProp(name = PROP_COLORBACKGROUND)
    public void setPropColorBackground(final ReactAdSenseView view, final String colorBackground) {
        view.setColorBackground(colorBackground);
    }

    @ReactProp(name = PROP_COLORADBORDER)
    public void setPropColorAdBorder(final ReactAdSenseView view, final String colorAdBorder) {
        view.setColorAdBorder(colorAdBorder);
    }

    @ReactProp(name = PROP_COLORBORDER)
    public void setPropColorBorder(final ReactAdSenseView view, final String colorBorder) {
        view.setColorBorder(colorBorder);
    }

    @ReactProp(name = PROP_NOTITLEUNDERLINE)
    public void setPropNoTitleUnderline(final ReactAdSenseView view, final String noTitleUnderline) {
        view.setNoTitleUnderline(Boolean.parseBoolean(noTitleUnderline));
    }

    @ReactProp(name = PROP_LONGERHEADLINES)
    public void setPropLongerHeadlines(final ReactAdSenseView view, final String longerHeadlines) {
        view.setLongerHeadlines(Boolean.parseBoolean(longerHeadlines));
    }

    @ReactProp(name = PROP_DETAILATTRIBUTION)
    public void setPropDetailedAttribution(final ReactAdSenseView view, final String detailedAttribution) {
        view.setDetailedAttribution(Boolean.parseBoolean(detailedAttribution));
    }

    @ReactProp(name = PROP_QUERY)
    public void setPropQuery(final ReactAdSenseView view, final String query) {
        view.setQuery(query);
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedViewConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("simulatorId", SearchAdRequest.DEVICE_ID_EMULATOR);
        return constants;
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("loadBanner", COMMAND_LOAD_BANNER);
    }

    @Override
    public void receiveCommand(ReactAdSenseView root, int commandId, @javax.annotation.Nullable ReadableArray args) {
        switch (commandId) {
            case COMMAND_LOAD_BANNER:
                root.loadBanner();
                break;
        }
    }
}
