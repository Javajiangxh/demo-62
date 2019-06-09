<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="js/easyui/themes/icon.css">
<script type="text/javascript" src="js/jquery-2.1.4.min.js">
</script>
<script type="text/javascript" src="js/easyui/jquery.easyui.min.js">
</script>
<script type="text/javascript" src="js/easyui/locale/easyui-lang-zh_CN.js">
</script>
<script type="text/javascript" src="ckeditor/ckeditor.js"></script>
<script type="text/javascript">
	function query(){
		$('#dg').datagrid('load',{
			author: $("#author").val(),
			title: $('#title').val(),
			label: $('#label').val()
		});
	}
	
	function openEdit(){
		$('#ff').form('clear');
		CKEDITOR.instances.content.setData('');
		$('#ti').attr("src","images/uploading.jpg");
		$('#dlg').dialog('open');
	}
	
	function save(){
		$('#ff').form('submit',{
			contentType: 'text/json,charset=utf-8',
			url: "article/save",
			onSubmit: function(){
				
			},
			success:function(data){
				data = JSON.parse(data);
				if(data.code == 1){
					$('#dg').datagrid('reload');
					$('#dlg').dialog('close');
				}else{
					for(var i=0;i<data.data.length;i++){
						alert(data.data[i].defaultMessage);
					}
				}
			}
		});
	}
	
	//列格式化函数
	function fmtop(value ,row ,index){
		return '<input type="button" value="修改" onclick=\'modify('+index+')\'>';
	}
	
	function fmtimgs(value,row,index){
		return "<img src='"+value+"',height='50px'>";
	}
	
	function modify(index){
		var row = $('#dg').datagrid('getRows')[index];
		
		if(row.titleimgs){
			$('#ti').attr("src",row.titleimgs);
		}else{
			$('#ti').attr("src","images/uploading.jpg");
		}
		
		$('#ff').form('load',row);
		CKEDITOR.instances.content.setData(row.content);
		$('#dlg').dialog('open');
	}
	
	function upload(){
		$.ajax({
			url: "article/upload",
			type: 'POST',
			cache: false,
			data: new FormData($('#ff')[0]),
			processData: false,
			contentType: false,
			dataType: "json",
			success: function(data){
				if(data.code == 1){
					$("#ti").attr("src",data.data);
					$("#titleImgs").val(data.data);
					$.messager.show({
						title:'系统提示',
						msg:data.msg,
						timeout:2000,
						showType:'slide'
					});
				}else{
					$.messager.alert('系统提示',data.msg,'error');
				}
			}
		});
	}
</script>
</head>
<body>
	<table class="easyui-datagrid" id="dg"
    data-options="
	    url:'article/query',
	    fitColumns:true,
	    singleSelect:true,
	    fit:true,
	    pagination:true,
	    pageSize:5,
	    pageList:[5,10,20],
	    rownumbers:true,
	    toolbar:'#tb'">
    <thead>
		<tr>
			<th data-options="field:'title',width:100">标题</th>
			<th data-options="field:'author',width:100">作者</th>
			<th data-options="field:'titleimgs',width:100,formatter:fmtimgs">操作</th>
			<th data-options="field:'id',width:100,formatter:fmtop">操作</th>
		</tr>
    </thead>
</table>

	<div id="tb" style="padding:5px;height:auto">
		<div>
			作者： <input class="easyui-textbox" style="width:80px" id="author">
			标题： <input class="easyui-textbox" style="width:80px" id="title">
			标签： 
			<input class="easyui-textbox" style="width:100px" id="label">
			<a onclick="query()" href="#" class="easyui-linkbutton" iconCls="icon-search">查询</a>
			<a onclick="openEdit()" href="#" class="easyui-linkbutton" iconCls="icon-add">添加</a>
		</div>
	</div>
	
	<h2>Toolbar and Buttons</h2>
	<p>The toolbar and buttons can be added to dialog.</p>
	<div style="margin:20px 0;">
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg').dialog('open')">Open</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg').dialog('close')">Close</a>
	</div>
	<div id="dlg" class="easyui-dialog" title="博文编辑" style="width:900px;height:400px;padding:10px"
			data-options="
				iconCls: 'icon-save',
				buttons: [{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){
						save();
					}
				},{
					text:'取消',
					handler:function(){
						$('#dlg').dialog('close');
					}
				}],
				closed:true,
				modal:true
			">
		<form id="ff" method="post">
			<input type="hidden" name="id" id="id">
			<input class="easyui-textbox" name="title" data-options="label:'标题'"><br>
			<input class="easyui-textbox" name="author" data-options="label:'作者'  " ><br>
			
			<img height="100px" alt="点击上传图片" src="images/browser.jpg" id="ti" onclick="file.click()">
			<input type="hidden" name="titleimgs" id="titleImgs">
			<input type="file" name="file" style="display: none;" id="file" onchange="upload()">
			
			内容：<textarea rows="5" cols="20" id="content" name="content"></textarea>
			<script>
				CKEDITOR.replace('content',{
					height:260,
					width:700,
				});
			</script>
		</form>
	</div>
</body>
</html>