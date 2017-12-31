package main;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedFileUpload;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;
import utils.FileUtils;
import utils.ServiceResult;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import api.CommentGet;
import api.CommentPut;
import api.Home;
import api.ImgGet;
import api.ImgUp;
import api.Login;
import api.MessageGetDetail;
import api.MessageGetList;
import api.MessagePut;
import api.ReMarkPutData;
import api.ShareCommonGet;
import api.ShareCommonPut;
import api.ShareDiaryGet;
import api.ShareDiaryPut;
import api.ShareListLoad;
import api.ShareMenuGet;
import api.ShareMenuPut;
import api.ShareOther;
import api.UserDetailChange;

import static io.netty.buffer.Unpooled.*;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;

public class HttpUploadServerHandler extends SimpleChannelInboundHandler<HttpObject> {
	private String Log = "----------start----------\n";

	private static final Logger logger = Logger.getLogger(HttpUploadServerHandler.class.getName());

	private FullHttpResponse response = null;

	private HttpRequest request;

	private boolean readingChunks;

	private HttpData partialContent;

	private final StringBuilder responseContent = new StringBuilder();

	private static final HttpDataFactory factory =
			new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); // Disk if size exceed

	private HttpPostRequestDecoder decoder;

	private URI uri;

	private Map<String, String> parmMap  = new HashMap<>();;

	static {
		DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file
		// on exit (in normal
		// exit)
		DiskFileUpload.baseDirectory = "img/"; // system temp directory
		DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on
		// exit (in normal exit)
		DiskAttribute.baseDirectory = "img/"; // system temp directory
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (decoder != null) {
			decoder.cleanFiles();
		}
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest request = this.request = (HttpRequest) msg;
			uri = new URI(request.uri());
			if (!request.uri().startsWith("/Img/Up")) {//非图片上传				
				if (msg instanceof HttpRequest){
					Log = Log +uri +"\n";
					if (true){
						//信息处理
						HttpMethod method = request.method();
						Log = Log + msg+" test\n";
						if (HttpMethod.GET.equals(method)) {
							// 是GET请求
							Log = Log + "GET:\n";
							QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
							decoder.parameters().entrySet().forEach( entry -> {
								// entry.getValue()是一个List, 只取第一个元素
								parmMap.put(entry.getKey(), entry.getValue().get(0));
								Log = Log + entry.getKey()+":"+entry.getValue().get(0)+"\n";
							});
							if (uri.getPath().startsWith("/img")){
								ImgGet imgGet = new ImgGet(parmMap);
								response = imgGet.setUri(uri.toString()).getResponse();
								Log = Log + imgGet.getLog();
							}
							if (uri.getPath().startsWith("/Share/Other")){
								ShareOther shareOther = new ShareOther(parmMap);
								shareOther.getResult(responseContent);
								Log = Log + shareOther.getLog();
								System.out.println(Log);
								ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
								// Build the response object.
								FullHttpResponse response = new DefaultFullHttpResponse(
										HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

								response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
								response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());

								// Write the response.
								ctx.channel().writeAndFlush(response);
								return;
							}
							if (uri.getPath().startsWith("/Home")){//主页
								Home home = new Home(parmMap);
								home.getResult(responseContent);
								Log = Log + home.getLog();
								System.out.println(Log);
								ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
								// Build the response object.
								FullHttpResponse response = new DefaultFullHttpResponse(
										HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

								response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
								response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());

								// Write the response.
								ctx.channel().writeAndFlush(response);
								return;
							}
							if (response ==null){//对未处理的接口进行"无接口"处理
								response = ServiceResult.getUnHandleResult();
							}
							if (HttpHeaders.isKeepAlive(request)) {  
								response.headers().set(CONNECTION, Values.KEEP_ALIVE);  
							}else {
								response.headers().set(CONNECTION, Values.CLOSE);  
							}
							response.headers().set(CONTENT_LENGTH, response.content().readableBytes()); 

							Log = Log +response.content().toString()+"\n";
							writeResponse(ctx.channel()); 
						}else if (request.method().equals(HttpMethod.POST)){
							try {
								decoder = new HttpPostRequestDecoder(factory, request);
							} catch (ErrorDataDecoderException e1) {
								e1.printStackTrace();
								responseContent.append(e1.getMessage());
								writeResponse(ctx.channel());
								ctx.channel().close();
								return;
							}
						}
					}else {
						Log = Log + "noful"+msg.toString()+"\n";
					}
				}else {
					Log = Log + "no"+msg.toString()+"\n";
				} 
				return;
			}
			responseContent.setLength(0);
			// new getMethod
			for (Entry<String, String> entry : request.headers()) {
				responseContent.append("HEADER: " + entry.getKey() + '=' + entry.getValue() + "\r\n");
			}
			responseContent.append("\r\n\r\n");

			// new getMethod
//			Set<Cookie> cookies;
//			String value = request.headers().get(HttpHeaderNames.COOKIE);
//			if (value == null) {
//				cookies = Collections.emptySet();
//			} else {
//				cookies = ServerCookieDecoder.STRICT.decode(value);
//			}
//			for (Cookie cookie : cookies) {
//				responseContent.append("COOKIE: " + cookie + "\r\n");
//			}
//			responseContent.append("\r\n\r\n");

			QueryStringDecoder decoderQuery = new QueryStringDecoder(request.uri());
			Map<String, List<String>> uriAttributes = decoderQuery.parameters();
			for (Entry<String, List<String>> attr: uriAttributes.entrySet()) {
				for (String attrVal: attr.getValue()) {
					responseContent.append("URI: " + attr.getKey() + '=' + attrVal + "\r\n");
				}
			}
			responseContent.append("\r\n\r\n");

			// if GET Method: should not try to create a HttpPostRequestDecoder
			if (request.method().equals(HttpMethod.GET)) {
				// GET Method: should not try to create a HttpPostRequestDecoder
				// So stop here
				responseContent.append("\r\n\r\nEND OF GET, MUST USE POST\r\n");
				// Not now: LastHttpContent will be sent writeResponse(ctx.channel());
				return;
			}
			try {
				decoder = new HttpPostRequestDecoder(factory, request);
			} catch (ErrorDataDecoderException e1) {
				e1.printStackTrace();
				responseContent.append(e1.getMessage());
				writeResponse(ctx.channel());
				ctx.channel().close();
				return;
			}

			readingChunks = HttpUtil.isTransferEncodingChunked(request);
			responseContent.append("Is Chunked: " + readingChunks + "\r\n");
			responseContent.append("IsMultipart: " + decoder.isMultipart() + "\r\n");
			if (readingChunks) {
				// Chunk version
				responseContent.append("Chunks: ");
				readingChunks = true;
			}
		}

		// check if the decoder was constructed before
		// if not it handles the form get
		if (decoder != null) {
			if (msg instanceof HttpContent) {
				// New chunk is received
				HttpContent chunk = (HttpContent) msg;
				try {
					decoder.offer(chunk);
				} catch (ErrorDataDecoderException e1) {
					e1.printStackTrace();
					responseContent.append(e1.getMessage());
					writeResponse(ctx.channel());
					ctx.channel().close();
					return;
				}
				responseContent.append('o');
				// example of reading chunk by chunk (minimize memory usage due to
				// Factory)
				readHttpDataChunkByChunk();
				// example of reading only if at the end
				if (chunk instanceof LastHttpContent) {
					if (!uri.getPath().startsWith("/Img/Up")){
						if (request.method().equals(HttpMethod.POST)) {
							// 是POST请求
							Log = Log + "POST:\n";
							for (Entry<String, String> entry : parmMap.entrySet()){
								Log = Log + entry.getKey()+":"+entry.getValue();
								}
							if (uri.getPath().startsWith("/Login")){//登录
								Login login = new Login(parmMap);
								response = login.getResponse();
								Log = Log + login.getLog();
							}else if (uri.getPath().startsWith("/ShareList/Load")) {//加载分享列表
								ShareListLoad shareListLoad = new ShareListLoad(parmMap);
								response = shareListLoad.getResponse();
								Log = Log +shareListLoad.getLog();
							}else if (uri.getPath().startsWith("/Message/Get/List")){//消息列表
								MessageGetList messageGet = new MessageGetList(parmMap);
								response = messageGet.getResponse();
								Log = Log + messageGet.getLog();
							}else if (uri.getPath().startsWith("/Message/Get/Detail")){//消息详情
								MessageGetDetail messageGetDetail = new MessageGetDetail(parmMap);
								response = messageGetDetail.getResponse();
								Log = Log + messageGetDetail.getLog();
							}else if (uri.getPath().startsWith("/Message/Put")){//发送消息
								MessagePut messagePut = new MessagePut(parmMap);
								response = messagePut.getResponse();
								Log = Log + messagePut.getLog();
							}else if (uri.getPath().startsWith("/ReMark/Put")){//点赞踩
								ReMarkPutData reMarkPutData = new ReMarkPutData(parmMap);
								response = reMarkPutData.getResponse();
								Log = Log + reMarkPutData.getLog();
							}else if (uri.getPath().startsWith("/User/ChangeDetail")){//修改用户信息
								UserDetailChange userDetailChange = new UserDetailChange(parmMap);
								response = userDetailChange.getResponse();
								Log = Log +userDetailChange.getLog();
							}else if(uri.getPath().startsWith("/Share/Diary/Put")){//日记分享
								ShareDiaryPut shareDiaryPut = new ShareDiaryPut(parmMap);
								response = shareDiaryPut.getResponse();
								Log = Log + shareDiaryPut.getLog();
							}else if(uri.getPath().startsWith("/Share/Common/Put")){//普通分享
								ShareCommonPut shareCommonPut = new ShareCommonPut(parmMap);
								response = shareCommonPut.getResponse();
								Log = Log + shareCommonPut.getLog();
							}else if(uri.getPath().startsWith("/Share/Menu/Put")){//食谱分享
								ShareMenuPut shareMenuPut = new ShareMenuPut(parmMap);
								response = shareMenuPut.getResponse();
								Log = Log + shareMenuPut.getLog();
							}else if(uri.getPath().startsWith("/Comment/Get")){//获取评论信息
								CommentGet commentGet = new CommentGet(parmMap);
								response = commentGet.getResponse();
								Log = Log + commentGet.getLog();
							}else if(uri.getPath().startsWith("/Comment/Put")){//发送评论
								CommentPut commentPut = new CommentPut(parmMap);
								response = commentPut.getResponse();
								Log = Log + commentPut.getLog();
							}else if(uri.getPath().startsWith("/Share/Common/Get")){//获取普通分享详情
								ShareCommonGet shareCommonGet = new ShareCommonGet(parmMap);
								response = shareCommonGet.getResponse();
								Log = Log + shareCommonGet.getLog();
							}else if(uri.getPath().startsWith("/Share/Diary/Get")){//获取日记详情
								ShareDiaryGet shareDiaryGet = new ShareDiaryGet(parmMap);
								response = shareDiaryGet.getResponse();
								Log = Log + shareDiaryGet.getLog();
							}else if(uri.getPath().startsWith("/Share/Menu/Get")){//获取菜谱详情
								ShareMenuGet shareMenuGet = new ShareMenuGet(parmMap);
								response = shareMenuGet.getResponse();
								Log = Log + shareMenuGet.getLog();
							}

						}
						if (response ==null){//对未处理的接口进行"无接口"处理
							response = ServiceResult.getUnHandleResult();
						}
						Log = Log +response.content().toString()+"\n";
						writeResponse(ctx.channel()); 
					}else {
						ImgUp imgUp = new ImgUp(parmMap);
						response = imgUp.getResponse();
						Log = Log + imgUp.getLog();
						writeResponse(ctx.channel());
						readingChunks = false;
						reset();
					}
				}
			}
		} else {
			Log = Log + "err\n";
			writeResponse(ctx.channel());
		}
	}

	private void reset() {
		request = null;

		// destroy the decoder to release all resources
		decoder.destroy();
		decoder = null;
	}

	/**
	 * Example of reading request by chunk and getting values from chunk to chunk
	 */
	private void readHttpDataChunkByChunk() {
		try {
			while (decoder.hasNext()) {
				InterfaceHttpData data = decoder.next();
				if (data != null) {
					// check if current HttpData is a FileUpload and previously set as partial
					if (partialContent == data) {
						logger.info(" 100% (FinalSize: " + partialContent.length() + ")");
						partialContent = null;
					}
					try {
						// new value
						writeHttpData(data);
					} finally {
						data.release();
					}
				}
			}
			// Check partial decoding for a FileUpload
			InterfaceHttpData data = decoder.currentPartialHttpData();
			if (data != null) {
				StringBuilder builder = new StringBuilder();
				if (partialContent == null) {
					partialContent = (HttpData) data;
					if (partialContent instanceof FileUpload) {
						builder.append("Start FileUpload: ")
						.append(((FileUpload) partialContent).getFilename()).append(" ");
					} else {
						builder.append("Start Attribute: ")
						.append(partialContent.getName()).append(" ");
					}
					builder.append("(DefinedSize: ").append(partialContent.definedLength()).append(")");
				}
				if (partialContent.definedLength() > 0) {
					builder.append(" ").append(partialContent.length() * 100 / partialContent.definedLength())
					.append("% ");
					logger.info(builder.toString());
				} else {
					builder.append(" ").append(partialContent.length()).append(" ");
					logger.info(builder.toString());
				}
			}
		} catch (EndOfDataDecoderException e1) {
			// end
			responseContent.append("\r\n\r\nEND OF CONTENT CHUNK BY CHUNK\r\n\r\n");
		}
	}

	private void writeHttpData(InterfaceHttpData data) {
		if (data.getHttpDataType() == HttpDataType.Attribute) {
			Attribute attribute = (Attribute) data;
			String value;
			try {
				value = attribute.getValue();
			} catch (IOException e1) {
				// Error while reading data from File, only print name and error
				e1.printStackTrace();
				responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": "
						+ attribute.getName() + " Error while reading value: " + e1.getMessage() + "\r\n");
				return;
			}
			if (value.length() > 100) {
				responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": "
						+ attribute.getName() + " data too long\r\n");
			} else {
				responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": "
						+ attribute + "\r\n");
			}
			String name = attribute.getName();
			parmMap.put(name, value);
			Log = Log +name +":"+value+"\n";
		} else {
			responseContent.append("\r\nBODY FileUpload: " + data.getHttpDataType().name() + ": " + data
					+ "\r\n");
			if (data.getHttpDataType() == HttpDataType.FileUpload) {
				FileUpload fileUpload = (FileUpload) data;
				File file = new File("img/UP"+System.nanoTime()
				+fileUpload.getFilename().substring(fileUpload.getFilename().lastIndexOf(".")));
				try{
				if (!file.exists()){
					file.createNewFile();
				}
					FileUtils.copyFile(fileUpload.getFile(), file);
				}catch (Exception e){
					e.printStackTrace();
				}
				parmMap.put(fileUpload.getName(), file.getAbsolutePath());
				if (fileUpload.isCompleted()) {
					if (fileUpload.length() < 10000) {
						responseContent.append("\tContent of file\r\n");
						try {
							responseContent.append(fileUpload.getString(fileUpload.getCharset()));
						} catch (IOException e1) {
							// do nothing for the example
							e1.printStackTrace();
						}
						responseContent.append("\r\n");
					} else {
						responseContent.append("\tFile too long to be printed out:" + fileUpload.length() + "\r\n");
					}
					// fileUpload.isInMemory();// tells if the file is in Memory
					// or on File
					// fileUpload.renameTo(dest); // enable to move into another
					// File dest
					// decoder.removeFileUploadFromClean(fileUpload); //remove
					// the File of to delete file
				} else {
					responseContent.append("\tFile to be continued but should not!\r\n");
				}
			}
		}
	}

	private void writeResponse(Channel channel) {
		// Convert the response content to a ChannelBuffer.
		ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
		responseContent.setLength(0);

		// Decide whether to close the connection or not.
		boolean close = request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)
				|| request.protocolVersion().equals(HttpVersion.HTTP_1_0)
				&& !request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);

		// Build the response object.
		if (response == null){
			try {
				if (uri.getPath().startsWith("/Img/Up")){
					response = ServiceResult.getUpSuccessResult("11");
				}else {
					response = ServiceResult.getUnHandleResult();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
		//        		new DefaultFullHttpResponse(
		//                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");

		if (!close) {
			// There's no need to add 'Content-Length' header
			// if this is the last response.
			response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
		}

//		Set<Cookie> cookies;
//		String value = request.headers().get(HttpHeaderNames.COOKIE);
//		if (value == null) {
//			cookies = Collections.emptySet();
//		} else {
//			cookies = ServerCookieDecoder.STRICT.decode(value);
//		}
//		if (!cookies.isEmpty()) {
//			// Reset the cookies if necessary.
//			for (Cookie cookie : cookies) {
//				response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
//			}
//		}
		// Write the response.
		ChannelFuture future = channel.writeAndFlush(response);
		// Close the connection after the write operation is done if necessary.
				if (close) {
		future.addListener(ChannelFutureListener.CLOSE);
				}
		Log = Log + "----------end----------\n";
		System.out.println(Log);
		parmMap.clear();
		Log = "----------start----------\n";
	}

	private void writeMenu(ChannelHandlerContext ctx) {
		// print several HTML forms
		// Convert the response content to a ChannelBuffer.
		responseContent.setLength(0);

		// create Pseudo Menu
		responseContent.append("<html>");
		responseContent.append("<head>");
		responseContent.append("<title>Netty Test Form</title>\r\n");
		responseContent.append("</head>\r\n");
		responseContent.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");

		responseContent.append("<table border=\"0\">");
		responseContent.append("<tr>");
		responseContent.append("<td>");
		responseContent.append("<h1>Netty Test Form</h1>");
		responseContent.append("Choose one FORM");
		responseContent.append("</td>");
		responseContent.append("</tr>");
		responseContent.append("</table>\r\n");

		// GET
		responseContent.append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		responseContent.append("<FORM ACTION=\"/formget\" METHOD=\"GET\">");
		responseContent.append("<input type=hidden name=getform value=\"GET\">");
		responseContent.append("<table border=\"0\">");
		responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
		responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
		responseContent
		.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
		responseContent.append("</td></tr>");
		responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
		responseContent.append("</table></FORM>\r\n");
		responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		// POST
		responseContent.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		responseContent.append("<FORM ACTION=\"/formpost\" METHOD=\"POST\">");
		responseContent.append("<input type=hidden name=getform value=\"POST\">");
		responseContent.append("<table border=\"0\">");
		responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
		responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
		responseContent
		.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
		responseContent.append("<tr><td>Fill with file (only file name will be transmitted): <br> "
				+ "<input type=file name=\"myfile\">");
		responseContent.append("</td></tr>");
		responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
		responseContent.append("</table></FORM>\r\n");
		responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		// POST with enctype="multipart/form-data"
		responseContent.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		responseContent.append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
		responseContent.append("<input type=hidden name=getform value=\"POST\">");
		responseContent.append("<table border=\"0\">");
		responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
		responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
		responseContent
		.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
		responseContent.append("<tr><td>Fill with file: <br> <input type=file name=\"myfile\">");
		responseContent.append("</td></tr>");
		responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
		responseContent.append("</table></FORM>\r\n");
		responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		responseContent.append("</body>");
		responseContent.append("</html>");

		ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
		// Build the response object.
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
		response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());

		// Write the response.
		ctx.channel().writeAndFlush(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.log(Level.WARNING, responseContent.toString(), cause);
		ctx.channel().close();
	}
}