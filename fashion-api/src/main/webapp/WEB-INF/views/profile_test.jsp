<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>프사 업로드 주방</title>
    <style>
        .preview-box { width: 200px; height: 200px; border: 2px dashed #ccc; margin-top: 10px; display: flex; align-items: center; justify-content: center; overflow: hidden; }
        .preview-box img { width: 100%; height: 100%; object-fit: cover; }
        .container { padding: 20px; font-family: 'Malgun Gothic', sans-serif; }
        input { margin-bottom: 10px; }
    </style>
</head>
<body>

<div class="container">
    <h2>프사 업로드</h2>
    
    <div>
        <label>회원 ID: </label>
        <input type="text" id="memberId" value="testUser">
    </div>

    <div>
        <label>사진 선택: </label>
        <input type="file" id="imageFile" accept="image/*">
    </div>

    <button type="button" onclick="handleUpload()">사진 올리기</button>

    <hr>

    <h3>결과 확인</h3>
    <div id="statusMsg">대기 중...</div>
    <div class="preview-box" id="resultView">
        <span>이미지 뜨는 곳</span>
    </div>
</div>

<script>
    async function handleUpload() {
        const id = document.getElementById('memberId').value;
        const file = document.getElementById('imageFile').files[0];
        const status = document.getElementById('statusMsg');
        const view = document.getElementById('resultView');

        if (!id || !file) {
            alert("ID랑 파일을 채워주세요.");
            return;
        }

        const formData = new FormData();
        formData.append('id', id);
        formData.append('file', file);

        status.innerText = "업로드 중...";

        try {
            const response = await fetch('/api/imageupload/profile', {
                method: 'POST',
                body: formData
            });

            const data = await response.json();
            console.log("주방에서 배달온 주소:", data.imageUrl);

            if (data.success) {
    status.innerHTML = `<b style="color:blue;">[성공]</b> ` + data.message;
    
    // 인쟈 아예 새 그릇을 만들어서 띡.. 얹어줍니다.
    const newImg = new Image();
    newImg.src = data.imageUrl + "?t=" + new Date().getTime();
    newImg.style.width = "200px";
    
    view.innerHTML = ""; // 인쟈 기존 글자 슥.. 비우고요
    view.appendChild(newImg); // 인쟈 새 사진을 슥.. 넣습니다.
} else {
                status.innerHTML = `<b style="color:red;">[실패]</b> ` + data.message;
            }
        } catch (err) {
            console.error(err);
            status.innerHTML = `<b style="color:red;">[에러]</b>`;
        }
    }
</script>

</body>
</html>