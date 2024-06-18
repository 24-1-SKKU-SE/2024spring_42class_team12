
chat1 = ''' 
[user] 챗봇으로 신고 방법
[AI]  1. 챗봇에게 신고할 시설물이 있는 캠퍼스, 건물, 강의실 번호를 입력해주세요. ex) "인문사회과학캠퍼스 경영관 33404 시설물 신고"
   2. 챗봇이 해당 내용을 바탕으로 신고 페이지에 강의실 정보를 입력해줄거에요.
   3. 나머지 신고에 필요한 내용을 신고 페이지에서 채워주세요. 사진과 상세 설명은 선택사항이에요.
   4. 신고에 필요한 부분을 입력하셨으면 신고하기 버튼을 눌러주세요.
   5. 신고가 완료되면 자동으로 내 신고페이지로 이동해요. 내 신고에서 신고내역을 확인할 수 있어요.

'''
chat2 = '''
[user] 챗봇으로 내 신고 조회 방법
[AI]  1. 챗봇에게 "내 신고 조회" 라고 입력해주세요.
   2. 챗봇이 내 신고 페이지로 이동시켜줄거에요.
   3. 내 신고 페이지에서 신고 내역을 조회할 수 있어요.

'''
chat3 = '''
[user] 챗봇으로 시설물 상태 조회 방법
[AI]  1. 챗봇에게 조회할 강의실의 캠퍼스, 건물, 강의실 번호를 입력해주세요. ex) "인문사회과학캠퍼스 경영관 33404  강의실 정보 조회"
   2. 챗봇이 해당 내용을 바탕으로 시설물 상태 페이지의 해당 강의실 정보 페이지로 이동시켜줄거에요.
   3. 강의실에서 초록색으로 표시된 부분이 사용가능한 상태를, 빨간 색으로 표시된 부분이 고장난 상태를 나타내요.

'''
chat4 = '''
[user] 시설물 담당자 연락처 조회 방법
[AI]  1. 챗봇에게 "시설물 담당자 연락처" 라고 입력해주세요.
   2. 챗봇이 시설물 담당자의 연락처를 알려줄거에요.


'''
chat_dict = {'1':chat1, '2':chat2, '3':chat3, '4':chat4}

import firebase_admin
from firebase_admin import credentials, firestore


def append_to_chat_history(user_token: str, FAQ_id: str, db):
    # Reference to the document
    doc_ref = db.collection('chatbot_users').document(user_token)

    # Get the document
    doc = doc_ref.get()
    if doc.exists:
        # Get the existing chat history
        chat_history = doc.to_dict().get('chathistory', '')
        
        # Append the new text to the chat history
        updated_chat_history = chat_history + chat_dict[FAQ_id]

        # Update the document with the new chat history
        doc_ref.update({
            'chathistory': updated_chat_history
        })
        print(f"Updated chat history for token {user_token}.")
        return chat_dict[FAQ_id]
    else:
        # Create a new document with the initial chat history
        doc_ref.set({
            'chathistory': chat_dict[FAQ_id]
        })
        print(f"Created new document for token {user_token} with initial chat history.")
        
        return chat_dict[FAQ_id]



