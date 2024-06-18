import firebase_admin
from firebase_admin import credentials, firestore
from doctest import Example
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate, FewShotChatMessagePromptTemplate
import re

classifier_llm = ChatOpenAI(model="gpt-3.5-turbo-0125",api_key="sk-proj-ord3wqZDvZj48Y4lAdBQT3BlbkFJbgoZjkwuSLILu72PNMYc", temperature=0.0)

def extract_info(response):
    # Regular expression to match the pattern of the response
    pattern = r"(\d{5})\s(.+)"
    match = re.match(pattern, response)
    if match:
        classroom_number = match.group(1)
        item_name = match.group(2)
        return classroom_number, item_name
    else:
        return None, None

#핵심부===============================================================================================
def AIresponse(chat):
    examples = [
    {"input": "자연과학캠퍼스 26302강의실 전등 신고", "output": "자연과학캠퍼스 26302 전등"}, 
    {"input": "자과캠 31062강의실 콘센트", "output": "자연과학캠퍼스 31062 콘센트"},
    {"input": "인문사회과학캠 경영관 50512강의실 콘센트 신고", "output": "인문사회과학캠퍼스 50512 콘센트"},
    {"input": "인문사회캠퍼스 33012 에어컨", "output": "인문사회과학캠퍼스 33012 에어컨"},
    {"input": "자연과학캠 호암관 55561강의실 시설물 신고", "output": "자연과학캠퍼스 55561 시설물"},
    {"input": "인사캠 26312강의실 의자 신고", "output": "인문사회과학캠퍼스 26312 의자"},
    {"input": "인문사회과학캠 경영관 33404 시설물 신고", "output": "인문사회과학캠퍼스 33404 시설물"},
    {"input": "인사캠 경영관 33512강의실 콘센트 신고", "output": "인문사회과학캠퍼스 33512 콘센트"},
    {"input": "자과캠 26001강의실 의자", "output": "자연과학캠퍼스 26001 의자"},
    {"input": "자과캠 수성관 13254 콘센트", "output": "자연과학캠퍼스 26001 의자"},
    {"input": "인문사회과학캠퍼스 2134 책상", "output": "인문사회과학캠퍼스 2134 책상"},
    {"input": "인문사회캠퍼스 33012 에어컨", "output": "인문사회과학캠퍼스 33012 에어컨"},
    {"input": "인문사회캠퍼스 경영관 01234강의실 의자 신고", "output": "인문사회과학캠퍼스 26312 의자"}
    ]

    # This is a prompt template used to format each individual example.
    example_prompt = ChatPromptTemplate.from_messages(
    [
        ("human", "{input}"),
        ("ai", "{output}"),
    ]
    )

    few_shot_prompt = FewShotChatMessagePromptTemplate(
        example_prompt=example_prompt,
        examples=examples,
    )

    final_prompt = ChatPromptTemplate.from_messages(
    [
        ("system", '''너는 성균관대학교 시설물관리 어플 인공지능이야. 사용자들의 신고하고자하는 캠퍼스, 강의실 번호와 물건 이름을 말해줘. 
        우리학교에는 자연과학캠퍼스와 인문사회과학캠퍼스가 있어. 학생들은 자과캠, 인사캠이라고도 불러. 이런점을 참고해서 자과캠이면 자연과학캠퍼스, 인사캠이면 인문사회과학캠퍼스로 대답해줘.
        또 성균관대학교 건물들에는 경영관,  다산경제관, 법학관, 수선관, 퇴계인문관,  호암관, 수성관, 제1공학관, 제2공학관, 자연과학관, 기초학문관, 생명공학관, 의학관, 약학관, 화학관, 반도체관이 있는데, 사용자가 입력하는 건물이름들은 무시해줘. 
        다음은 대화의의 예시야.'''),
        few_shot_prompt,
        ("human", "{input}"),
    ]
    )

    chain = final_prompt | classifier_llm

    response = chain.invoke({"input": chat}).content
    components = response.split()
    campus = components[0]
    classroom = components[1]
    item = components[2]
    return campus, classroom, item
#핵심부===============================================================================================

def append_to_chat_history(user_token: str, text: str, db):
    response = AIresponse(text)
    components = response.split()
    try:
        campus = components[0]
        classroom_num = components[1]
        item = components[2]
        print(campus, classroom_num, item)  
    except:
        print("신고 에러")  
        return "", "", ""
    
    
    if campus not in ["자연과학캠퍼스", "인문사회과학캠퍼스"] or not (classroom_num.isdigit() and 4 <= len(classroom_num) <= 5):
        return "", "", "" 
    chathistory = f'''
[user] {text}
'''
    doc_ref = db.collection('chatbot_users').document(user_token)

    # Get the document
    doc = doc_ref.get()
    if doc.exists:
        # Get the existing chat history
        chat_history = doc.to_dict().get('chathistory', '')
        
        # Append the new text to the chat history
        updated_chat_history = chat_history + chathistory

        # Update the document with the new chat history
        doc_ref.update({
            'chathistory': updated_chat_history
        })
        print(f"Updated chat history for token {user_token}.")
        return campus, classroom_num, item
    else:
        # Create a new document with the initial chat history
        doc_ref.set({
            'chathistory': chathistory
        })
        print(f"Created new document for token {user_token} with initial chat history.")
        return campus, classroom_num, item



