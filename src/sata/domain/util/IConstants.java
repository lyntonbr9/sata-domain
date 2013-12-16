package sata.domain.util;

import java.math.BigDecimal;
import java.util.Locale;

public interface IConstants {

	public int CANDLE_VERDE = 4;
	public int CANDLE_VERMELHA = 3;
	public String NOVA_LINHA = System.getProperty("line.separator");
	
	public String mesesYahooFinances[] = {"jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"};
	
	public String[] SERIES_CALL = {"A","B","C","D","E","F","G","H","I","J","K","L"};
	
	public char[] SERIES_CALL_CHAR = {'A','B','C','D','E','F','G','H','I','J','K','L'};
	
	public String[] SERIES_PUT = {"M","N","O","P","Q","R","S","T","U","V","W","X"};
	
	public String ARQ_SATA_CONF = "sata-conf";
	
	public String PROP_SATA_AMBIENTE="sata.ambiente";
	
	public String PROP_SATA_BD="sata.bd.";
	public String PROP_SATA_DB_JDBC_DRIVER = "sata.db.jdbc.driver.";
	public String PROP_SATA_DB_URL="sata.db.url.";
	public String PROP_SATA_DB_USERNAME="sata.db.username.";
	public String PROP_SATA_DB_PASSWORD="sata.db.password.";
	public String PROP_SATA_DB_MAXPOOLSIZE="sata.db.maxpoolsize";
	
	public String PROP_PCTGEM_VARIACAOALTAPOUCOTEMPO = "pctgem.variacaoAltaPoucoTempo";
	public String PROP_CANDLE_PCTGEMPAVIOGRANDE = "candlestick.pctgem.paviogrande";
	
	public String PROP_VALORMAX_OPCAO_PUT = "opcao.put.valormaximo";
	public String PROP_QTD_DIAS_FALTAM_VENCIMENTO_PUT = "opcao.put.qtdDiasFaltamParaVencimento";
	public String PROP_VALORMAX_OPCAO_CALL = "opcao.call.valormaximo";
	public String PROP_QTD_DIAS_FALTAM_VENCIMENTO_CALL = "opcao.call.qtdDiasFaltamParaVencimento";
	
	public String BD_POSTGRE = "postgre";
	public String BD_MYSQL = "mysql";
	public String BD_HIBERNATE = "hibernate";
	
	public String AMBIENTE_PROD = "prod";
	public String AMBIENTE_DESENV = "desenv";
	
	public int QTD_DIAS_MES = 30;
	public int QTD_DIAS_ANO = 365;
	public int QTD_DIAS_FALTA_1_MES_VENC = 29; //quando for saber 1 mes
	public int QTD_DIAS_FALTA_2_MES_VENC = 59; //quando for saber 2 meses
	public double TAXA_DE_JUROS = 0.075; //8,0%
	public double PCTGEM_OPCAO = 0.05;
	public int QTD_DIAS_UTEIS_ANO = 252;
	public int QTD_DIAS_UTEIS_MES=21;
	
	double SPREAD = 2.5;
	char COMPRADO = 'C';
	char VENDIDO = 'V';
	int ABERTURA = 0;
	int FECHAMENTO = 1;
	
	Locale LOCALE_BRASIL = new Locale("pt", "BR");
	Locale LOCALE_EUA = new Locale("en");
	Locale LOCALE_DEFAULT = LOCALE_BRASIL;
	
	BigDecimal CEM = new BigDecimal(100);
	
	String MSG_BUNDLE = "msg/messages";
	
	String MSG_GENERAL_LABEL_ALTERAR = "general.label.update";
	String MSG_GENERAL_LABEL_INCLUIR = "general.label.add";
	String MSG_GENERAL_LABEL_ACAO = "general.label.acao";
	String MSG_GENERAL_LABEL_POSICAO = "general.label.posicao";
	String MSG_GENERAL_LABEL_QTD = "general.label.qtd";
	String MSG_GENERAL_LABEL_ATIVO = "general.label.ativo";
	String MSG_GENERAL_LABEL_OPERACAO = "general.label.operacao";
	String MSG_GENERAL_LABEL_OF = "general.label.of";
	String MSG_GENERAL_LABEL_NOME = "general.label.nome";
	String MSG_GENERAL_LABEL_VALOR_INVESTIDO = "general.label.valorInvestido";
	String MSG_GENERAL_LABEL_PRECO_ACAO = "general.label.precoAcao";
	String MSG_GENERAL_LABEL_INVESTIDOR = "general.label.investidor";
	String MSG_GENERAL_LABEL_VOLATILIDADE = "general.label.volatilidade";
	String MSG_GENERAL_LABEL_DATA_VENCIMENTO = "general.label.dataVencimento";
	String MSG_GENERAL_LABEL_OPCAO = "general.label.opcao";
	String MSG_OPERACOES_LABEL_MESES = "operacoes.meses.label";
	String MSG_OPERACOES_LABEL_ORDEM = "operacoes.ordem.label";
	String MSG_SIMULACAO_LABEL_MEDIA_ANUAL = "simulacao.resultado.mediaAnual";
	String MSG_SIMULACAO_LABEL_MEDIA_MENSAL = "simulacao.resultado.mediaMensal";
	String MSG_SIMULACAO_LABEL_VALOR_INICIAL = "simulacao.resultado.valorInicial";
	String MSG_SIMULACAO_LABEL_VALOR_FINAL = "simulacao.resultado.valorFinal";
	String MSG_ALERTA_LABEL_ALERTA = "alerta.alerta.label";
	String MSG_ALERTA_LABEL_SERIE = "alerta.serie.label";
	String MSG_ALERTA_LABEL_VALOR_SERIE = "alerta.valorSerie.label";
	String MSG_ALERTA_LABEL_PREC_SERIE = "alerta.percSerie.label";
	String MSG_ALERTA_LABEL_PERC_GANHO = "alerta.formAlerta.percGanho.label";
	String MSG_ALERTA_LABEL_PERC_PERDA = "alerta.formAlerta.percPerda.label";
	String MSG_ALERTA_LABEL_DATA_EXECUCAO = "alerta.formSerie.dataExecucao.label";
	String MSG_ALERTA_LABEL_QTD_LOTES_ACAO = "alerta.formSerie.qtdLotesAcao.label";
	String MSG_ALERTA_LABEL_VALOR = "alerta.formOp.valor.label";
	String MSG_ALERTA_EMAIL_ASSUNTO_GANHO = "alerta.email.assunto.ganho";
	String MSG_ALERTA_EMAIL_ASSUNTO_PERDA = "alerta.email.assunto.perda";
	String MSG_ACOMPANHAMENTO_LABEL_ACOMPANHAMENTO = "acompanhamento.acompanhamento.label";
	String MSG_ACOMPANHAMENTO_LABEL_PERC_TOLERANCIA_INFERIOR = "acompanhamento.formAcompOpcao.percToleranciaInferior.label";
	String MSG_ACOMPANHAMENTO_LABEL_PERC_TOLERANCIA_SUPERIOR = "acompanhamento.formAcompOpcao.percToleranciaSuperior.label";
	String MSG_ACOMPANHAMENTO_MSG_NAO_ENCONTRADA = "acompanhamento.msg.naoEncontrada";
	String MSG_ACOMPANHAMENTO_MSG_NENHUMA_OPCAO_ENCONTRADA = "acompanhamento.msg.nenhumaOpcaoEncontrada";
	String MSG_ACOMPANHAMENTO_EMAIL_ASSUNTO = "acompanhamento.email.assunto";
	String MSG_LOGIN_LABEL_EMAIL = "login.email.label";
	String MSG_LOGIN_LABEL_SENHA = "login.senha.label";
	String MSG_ENUM_LABEL_COMPRA = "enum.posicao.C";
	String MSG_ENUM_LABEL_VENDA = "enum.posicao.V";
	
	String MSG_PATTERN_OPERACAO = "toString.operacao.pattern";
	String MSG_PATTERN_OPERACAO_MESES = "toString.operacao.pattern.meses";
	String MSG_PATTERN_OPERACAO_DIAS = "toString.operacao.pattern.dias";
	String MSG_PATTERN_PRECO_ACAO = "toString.precoAcao.pattern";
	String MSG_PATTERN_PRECO_OPCAO = "toString.precoOpcao.pattern";
	String MSG_PATTERN_PRECO_RENDA_FIXA = "toString.precoRendaFixa.pattern";
	
	String MSG_ENUM_PREFIX_ATRIBUTO = "enum.atributo.";
	String MSG_ENUM_PREFIX_TIPO_RELATORIO = "enum.tipoRelatorio.";
	String MSG_ENUM_PREFIX_TIPO_CALCULO_VALOR_INVESTIDO = "enum.tipoCalculoValorInvestido.";
	String MSG_ENUM_PREFIX_POSICAO = "enum.posicao.";
	
	String MSG_ERRO_GENERICO = "general.msg.error.generico";
	String MSG_ERRO_CAMPO_OBRIGATORIO = "general.msg.error.campoObrigatorio";
	String MSG_ERRO_VALOR_MAIOR_QUE_ZERO = "general.msg.error.maiorQueZero";
	String MSG_ERRO_ERRO_EMAIL_NAO_CADASTRADO = "login.msg.error.emailNaoCadastrado";
	String MSG_ERRO_ERRO_SENHA_INCORRETA = "login.msg.error.senhaIncorreta";
}

