import firebase_admin
from firebase_admin import credentials, firestore
from doctest import Example
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate, FewShotChatMessagePromptTemplate
import re

classifier_llm = ChatOpenAI(model="gpt-3.5-turbo-0125",api_key="sk-proj-ord3wqZDvZj48Y4lAdBQT3BlbkFJbgoZjkwuSLILu72PNMYc", temperature=0.0)

#핵심부===============================================================================================
def AIresponse(chat):
    examples = [
    {"input": "자과캠 26302강의실 조회", "output": "자연과학캠퍼스 26302"}, 
    {"input": "자연과학캠퍼스 31062강의실", "output": "자연과학캠퍼스 31062"},
    {"input": "인사캠 33012", "output": "인문사회과학캠퍼스 33012"},
    {"input": "인문사회과학캠퍼스 수성관 26312강의실 정보", "output": "인문사회과학캠퍼스 26312"},
    {"input": "자연과학캠퍼스 호암관 26001강의실 의자", "output": "자연과학캠퍼스 26001"},
    {"input": "자과캠 호암관 30123강의실 콘센트", "output": "자연과학캠퍼스 30123"}
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
        ("system", '''너는 성균관대학교 시설물관리 어플 인공지능이야. 사용자들의 조회하고자하는 캠퍼스와 강의실 번호를 말해줘. 
        우리학교에는 자연과학캠퍼스와 인문사회과학캠퍼스가 있어. 학생들은 자과캠, 인사캠이라고도 불러. 이런점을 참고해서 자과캠이면 자연과학캠퍼스, 인사캠이면 인문사회과학캠퍼스로 대답해줘.
        또 성균관대학교 건물들에는 경영관,  다산경제관, 법학관, 수선관, 퇴계인문관,  호암관, 수성관, 제1공학관, 제2공학관, 자연과학관, 기초학문관, 생명공학관, 의학관, 약학관, 화학관, 반도체관이 있는데, 사용자가 입력하는 건물이름들은 무시해줘. 
        다음은 대화의의 예시야.사용자들의 정보 조회를 하려는 강의실 번호를 말해줘'''),
        few_shot_prompt,
        ("human", "{input}"),
    ]
    )

    chain = final_prompt | classifier_llm
    return chain.invoke({"input": chat}).content
    
#핵심부===============================================================================================
def append_to_chat_history(user_token: str, text: str, db):
    result = AIresponse(text)
    components = result.split()
    try:
        campus = components[0]
        classroom_num = components[1]
        print(campus, classroom_num)  
    except:
        print("강의실 조회 에러")  
        campus = ""
        classroom_num = ""
    
    
    if campus not in ["자연과학캠퍼스", "인문사회과학캠퍼스"] or not (classroom_num.isdigit() and 4 <= len(classroom_num) <= 5)or(campus==""):
        response =  '''잘못된 형식입니다. 강의실 조회를 위해서는 \"OOOO캠퍼스 OOOOO강의실 조회\"형식으로 말씀해주세요. 
        에시) 자연과학캠퍼스 31265강의실 조회'''
        chathistory = f'''
[user] {text}
[AI] 잘못된 형식입니다. 강의실 조회를 위해서는 \"OOOO캠퍼스 OOOOO강의실 조회\"형식으로 말씀해주세요. 
        에시) 자연과학캠퍼스 31265강의실 조회
''' 
        campus = ""
        classroom_num = ""
    else:
        chathistory = f'''
[user] {text}
'''
    print(f"강의실 조회 캠퍼스: {campus} 강의실: {classroom_num} ")
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
        return "", campus, classroom_num
    else:
        # Create a new document with the initial chat history
        doc_ref.set({
            'chathistory': chathistory
        })
        print(f"Created new document for token {user_token} with initial chat history.")
        return "", campus, classroom_num



