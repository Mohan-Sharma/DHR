<!doctype html>
<html>
    <head> 
        <title>Viewing Case Sheet Document</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
        <style type="text/css" media="screen"> 
			html, body	{ height:100%; }
			body { margin:0; padding:0; overflow:auto; }   
			#flashContent { display:none; }
        </style> 
		
		<link rel="stylesheet" type="text/css" href="org/css/flexpaper.css" />
		<script type="text/javascript" src="js/jquery.min.js"></script>
		<script type="text/javascript" src="js/jquery.extensions.min.js"></script>
		<script type="text/javascript" src="js/flexpaper.js"></script>
		<script type="text/javascript" src="js/flexpaper_handlers_debug.js"></script>
    </head> 
    <body>
    <div id="FlexPaperViewer" class="flexpaper_viewer" style="position:absolute;left:10px;top:10px;width:770px;height:770px"></div>

        <script type="text/javascript">

         function goto10(){
             console.log("Page 10000 " + getDocViewer())
             $FlexPaper('FlexPaperViewer').gotoPage(10)
         }

        var docUrl = "download?inPatientNumber=<%=request.getParameter("inPatientNumber")%>"
        var startDocument = "Paper";
        $.support.cors=true


        $('#FlexPaperViewer').FlexPaperViewer(
                { config : {
                    PDFFile : docUrl,
					key : "$8920cbcabb659dc1319",
                    Scale : 0.6,
                    ZoomTransition : 'easeOut',
                    ZoomTime : 0.5,
                    ZoomInterval : 0.2,
                    FitPageOnLoad : true,
                    FitWidthOnLoad : false,
                    FullScreenAsMaxWindow : false,
                    ProgressiveLoading : false,
                    MinZoomSize : 0.2,
                    MaxZoomSize : 5,
                    SearchMatchAll : false,

                    RenderingOrder : 'html5, flash',

                    ViewModeToolsVisible : true,
                    ZoomToolsVisible : true,
                    NavToolsVisible : true,
                    CursorToolsVisible : true,
                    SearchToolsVisible : true,
                    WMode : 'window',
                    localeChain: 'en_US'
                }}
        );
        </script>

	</div>
   </body>
</html> 