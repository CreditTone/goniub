package com.deep007.goniub.request;

/**
 * 网页类型Request的表示。
 *
 */
public final class PageRequest extends HttpRequest {
	
	/**
	 * 网页编码
	 */
	public enum PageEncoding{
    	UTF8,
    	GBK,
    	GB2312,
  //  	AUTO;
    }

    private PageEncoding pageEncoding = PageEncoding.UTF8;;
    
    
    
    public PageRequest(){
    	method = Method.GET;
    }
    
	public PageRequest(String url) {
		this.url = url;
	}

	public PageEncoding getPageEncoding() {
		return pageEncoding;
	}

	public void setPageEncoding(PageEncoding pageEncoding) {
		if(pageEncoding != null){
			this.pageEncoding = pageEncoding;
		}
	}
	
}
