import json
import pandas as pd
from ditto_light.summarize import Summarizer
from ditto_light.knowledge import *
import matcher
import argparse
import jsonlines

def toInputFormat(dic):
    res = ""
    for i in dic:
        if res != "":
            res += " "
        res += "COL " + i + " " + "VAL " + dic[i]
    return res

def toOutputFormat(str):
    str = " " + str
    lst = str.split(" COL ")
    lst.remove(lst[0])
    res = {}
    for i in lst:
        lst2 = i.split(" VAL ")

        res[lst2[0]] = lst2[1]
    return res

def preprocess(data):
    print("preprocess")
    right_str = data
    lines = open('input/library.jsonl', encoding='utf-8')
    with open('input/input.jsonl', 'w', encoding='utf-8') as fout:
        for line in lines:
            line = line.rstrip('\n')
            s = "[\"" + line + "\", \"" + data + " \"]\n"
            fout.write(s)
    print("preprocess done")

def print_result(path):
    print("save predictions done")
    df = pd.DataFrame()
    with open(path, "r+", encoding="utf8") as f:
        for item in jsonlines.Reader(f):
            dic = toOutputFormat(item['left'])
            dic['match_confidence'] = item['match_confidence']
            dic['match'] = item['match']
            df_dictionary = pd.DataFrame([dic])
            df = pd.concat([df, df_dictionary], ignore_index=True)
    df_match = df.loc[df['match'] == 1]
    df_match_ranked = df_match.sort_values(by="match_confidence", axis=0, ascending=False)
    df_not_match = df.loc[df['match'] == 0]
    df_not_match_ranked = df_not_match.sort_values(by ="match_confidence", axis=0, ascending=True)
    df_final = pd.concat([df_match_ranked, df_not_match_ranked], axis= 0, ignore_index=True)
    df_final = df_final.head()
    for index, row in df_final.iterrows():
        print(row.to_json())

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--task", type=str, default='Structured/iTunes-Amazon')
    parser.add_argument("--input_path", type=str, default='input/input.jsonl')
    parser.add_argument("--output_path", type=str, default='output/output.jsonl')
    parser.add_argument("--lm", type=str, default='distilbert')
    parser.add_argument("--use_gpu", dest="use_gpu", action="store_true")
    parser.add_argument("--fp16", dest="fp16", action="store_true")
    parser.add_argument("--checkpoint_path", type=str, default='checkpoints/')
    parser.add_argument("--dk", type=str, default=None)
    parser.add_argument("--summarize", dest="summarize", action="store_true")
    parser.add_argument("--max_len", type=int, default=256)
    hp = parser.parse_args()

    data = {}
    with open(r"temp.txt", 'r') as file:
        for line in file:
            data = json.loads(line)
            break

    # data = json.loads()
    input_data = toInputFormat(data)
    preprocess(input_data)

    matcher.set_seed(123)
    config, model = matcher.load_model(hp.task, hp.checkpoint_path,
                       hp.lm, True, hp.fp16)

    summarizer = dk_injector = None
    if hp.summarize:
        summarizer = Summarizer(config, hp.lm)

    if hp.dk is not None:
        if 'product' in hp.dk:
            dk_injector = ProductDKInjector(config, hp.dk)
        else:
            dk_injector = GeneralDKInjector(config, hp.dk)

    # tune threshold
    threshold = matcher.tune_threshold(config, model, hp)

    # run prediction
    matcher.predict(hp.input_path, hp.output_path, config, model,
            summarizer=summarizer,
            max_len=hp.max_len,
            lm=hp.lm,
            dk_injector=dk_injector,
            threshold=threshold)

    # print result
    pd.set_option('expand_frame_repr', False)
    print_result('output/output.jsonl')




