from doctest import Example
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate, FewShotChatMessagePromptTemplate

classifier_llm = ChatOpenAI(model="gpt-3.5-turbo-0125",api_key="sk-proj-ord3wqZDvZj48Y4lAdBQT3BlbkFJbgoZjkwuSLILu72PNMYc", temperature=0.0)


def chat_type_classifier(chat):

    examples = [
    {"input": "고장난 물건을 신고하고 싶어", "output": "report"}, 
    {"input": "33019강의실 콘센트 정보 조회", "output": "FAC"},
    {"input": "자과캠 33126강의실 콘센트 신고해줘", "output": "real_report"},
    {"input": "고장난 물건을 어떻게 신고할 수 있니?", "output": "NORMAL"},
    {"input": "26312강의실에는 콘센트가 몇개나 있니?", "output": "NORMAL"},
    {"input": "자연과학캠퍼스 수성관 26012강의실 정보 조회", "output": "FAC"},
    {"input": "26312강의실에 의자가 고장났어", "output": "report"},
    {"input": "인사캠 35162강의실 콘센트", "output": "real_report"},
    {"input": "자연과학캠퍼스 23120강의실 조회", "output": "FAC"},
    {"input": "30418강의실에 불이 안들어와", "output": "report"},
    {"input": "관리자에게 연락하고 싶어", "output": "NORMAL"},
    {"input": "어플 사용법을 알려줘", "output": "NORMAL"},
    {"input": "26012강의실 조회", "output": "FAC"},
    {"input": "33012강의실 신고하고 싶어", "output": "report"}, 
    {"input": "26312강의실에 에어컨이 안들어와", "output": "report"},
    {"input": "인문사회캠퍼스 자연과학관 시설물 신고", "output": "report"},
    {"input": "인문사회캠퍼스 수성관 의자 신고", "output": "report"},
    {"input": "33126강의실 정보를 알려줘", "output": "NORMAL"},
    {"input": "인사캠 호암관 30123강의실 정보", "output": "FAC"},
    {"input": "자과캠 26012강의실 콘센트 신고", "output": "real_report"},
    {"input": "26012강의실", "output": "FAC"},
    {"input": "내 신고 내역", "output": "MY_REPORT"},
    {"input": "강의실 조회", "output": "FAC"},
    {"input": "어플 사용법좀 알려줘", "output": "NORMAL"},
    {"input": "string", "output": "unidentified"},
    {"input": "33201강의실", "output": "FAC"},
    {"input": "asdgfhsfsgfdgf32123", "output": "unidentified"},
    {"input": "오늘 날씨는 어때", "output": "unidentified"},
    {"input": "강의실이 너무 더워", "output": "report"},
    {"input": "26312강의실 정보", "output": "FAC"},
    {"input": "안녕", "output": "NORMAL"},
    {"input": "자연과학캠퍼스 26312강의실 에어컨 신고", "output": "real_report"},
    {"input": "인문사회캠퍼스 15204강의실 콘센트 신고", "output": "real_report"},
    {"input": "나의 신고 내역을 보여줘", "output": "MY_REPORT"},
    {"input": "자연과학캠퍼스 수성관 시설물 신고", "output": "report"}

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
        ("system", "너는 성균관대학교 시설물관리 어플 인공지능이야. 사용자들이 어플의 어떤 기능을 원하는지 알려줘. 주의해야할 점은 만약 사용자가 시설물 신고를 원하는데 \"31232강의실\"과같이 강의실 번호를 말하지 않으면 무조건 \"report\"라고 대답해줘. 다음은 대화의 예시야. "),
        few_shot_prompt,
        ("human", "{input}"),
    ]
    )

    chain = final_prompt | classifier_llm
    result = chain.invoke({"input": chat}).content
    print(result)
    return result

    '''
    신고유형
    고장신고:           report, report_fault
    어플 사용법 설명:    about_app
    강의실 정보:         about_classroom
    알 수 없음 :        unidentified
    '''


    