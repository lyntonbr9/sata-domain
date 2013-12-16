package sata.domain.to;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.CompareToBuilder;

import sata.auto.operacao.ativo.Opcao;
import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.auto.to.Dia;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.util.SATAUtil;
import sata.metastock.util.BlackScholes;

@Entity
@Table(name="CotacaoOpcao")
public class CotacaoOpcaoTO implements TO, Comparable<CotacaoOpcaoTO>, Serializable {

	private static final long serialVersionUID = -6712687492264590910L;

	@Id @Column
	private String codigo;

	@Id @Column
	private String periodo;
	
	@Column
	private String codigoAcao;
	
	@Column
	private String abertura;
	
	@Column
	private String maxima;
	
	@Column
	private String minima;
	
	@Column
	private String fechamento;
	
	@Column
	private String precoExercicio;

	@Column
	private String dataVencimento;

	@Column
	private String valorIntrinseco;
	
	@Column
	private String valorExtrinseco;

	@Column
	private String tipoPeriodo;
	
	@Column
	private String ano;
	
	@Column
	private String volume;
	
	@Column
	private double volatilidadeImplicita;
	
	@Column
	private double volatilidadeAnual;
	
	@Column
	private double volatilidadeMensal;
	
	@Column
	private boolean ehATM;

	@Transient
	private int split;

	@Transient
	private ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
	
	@Override
	public String getId() {
		return codigo + "-" + periodo;
	}

	public BigDecimal getValorFechamento() {
		return new BigDecimal(Double.parseDouble(fechamento)/(100*split));
	}
	
	public BigDecimal getValorPrecoExercicio() {
		return new BigDecimal(Double.parseDouble(precoExercicio)/(100*split));
	}
	
	public BigDecimal getValorIntrinseco() {
		return new BigDecimal(Double.parseDouble(valorIntrinseco));
	}
	
	public BigDecimal getValorExtrinseco() {
		return new BigDecimal(Double.parseDouble(valorExtrinseco));
	}
	
	public BigDecimal getValorVolatilidadeImplicita() {
		return new BigDecimal(volatilidadeImplicita);
	}
	
	public BigDecimal getValorVolatilidadeAnual() {
		return new BigDecimal(volatilidadeAnual);
	}
	
	public BigDecimal getValorVolatilidadeMensal() {
		return new BigDecimal(volatilidadeMensal);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof CotacaoOpcaoTO))
			return false;
		return ((CotacaoOpcaoTO)other).codigo.equals(codigo) 
			&& ((CotacaoOpcaoTO)other).periodo.equals(periodo);
	}
	
	@Override
	public int compareTo(CotacaoOpcaoTO other) {
		return new CompareToBuilder()
	       .append(codigo, other.codigo)
	       .append(periodo, other.periodo)
	       .toComparison();
	}
	
	@Override
	public String toString() {
		return codigo + " " + periodo + " = " + fechamento;
	}
	
	public double getVolatilidadeAnual() {
		return volatilidadeAnual;
	}

	public void setVolatilidadeAnual(double volatilidadeAnual) {
		this.volatilidadeAnual = volatilidadeAnual;
	}

	public double getVolatilidadeMensal() {
		return volatilidadeMensal;
	}

	public void setVolatilidadeMensal(double volatilidadeMensal) {
		this.volatilidadeMensal = volatilidadeMensal;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(String tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public String getAbertura() {
		return abertura;
	}

	public void setAbertura(String abertura) {
		this.abertura = abertura;
	}

	public String getMaxima() {
		return maxima;
	}

	public void setMaxima(String maxima) {
		this.maxima = maxima;
	}

	public String getMinima() {
		return minima;
	}

	public void setMinima(String minima) {
		this.minima = minima;
	}

	public String getFechamento() {
		return fechamento;
	}

	public void setFechamento(String fechamento) {
		this.fechamento = fechamento;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getPrecoExercicio() {
		return precoExercicio;
	}

	public void setPrecoExercicio(String precoExercicio) {
		this.precoExercicio = precoExercicio;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public double getVolatilidadeImplicita() {
		return volatilidadeImplicita;
	}

	public void setVolatilidadeImplicita(double volatilidadeImplicita) {
		this.volatilidadeImplicita = volatilidadeImplicita;
	}

	public void setValorIntrinseco(String valorIntrinseco) {
		if (valorIntrinseco.length() > 10)
			this.valorIntrinseco = valorIntrinseco.substring(0,10);
		else
			this.valorIntrinseco = valorIntrinseco;
	}

	public void setValorExtrinseco(String valorExtrinseco) {
		if (valorExtrinseco.length() > 10)
			this.valorExtrinseco = valorExtrinseco.substring(0,10);
		else
			this.valorExtrinseco = valorExtrinseco;
	}
	
	public String getCodigoAcao() {
		return codigoAcao;
	}

	public void setCodigoAcao(String codigoAcao) {
		this.codigoAcao = codigoAcao;
	}
	
	public int getSplit() {
		return split;
	}

	public void setSplit(int split) {
		this.split = split;
	}
	
	public boolean isEhATM() {
		return ehATM;
	}

	public void setEhATM(boolean ehATM) {
		this.ehATM = ehATM;
	}
	
	/**
	 * Calcula os valores (VI, VE, Volatilidade Implicita) da cotação da opção.
	 */
	public void processaValores(){
	
		try {
			//verifica se a opcao eh call(true) ou put(false)
			boolean tipoOpcao = Opcao.getTipoOpcao(this.codigo);
			
			CotacaoAtivoTO cotacaoAcao = null;

			Dia diaPeriodo = new Dia(this.periodo);
			Dia diaVencimento = new Dia(this.dataVencimento);
			
			//consulta o preco da acao no periodo da opcao
			cotacaoAcao = caDAO.getCotacaoDoAtivo(this.codigoAcao, diaPeriodo.formatoBanco());
			
			//atualiza o split da opcao utilizando o split da acao
			this.setSplit(cotacaoAcao.getSplit());
			
			//recupera o valor de fechamento da acao
			BigDecimal valorFechamentoAcao = cotacaoAcao.getValorFechamento();
			
			//recupera o preco de exercicio da opcao em double, usando o split da acao
			double precoExercicioOpcao = this.getValorPrecoExercicio().doubleValue();
			
			//recupera o valor da opcao em double, usando o split da acao
			double valorOpcao = this.getValorFechamento().doubleValue();
			
			//calcula o VI
			double vi = BlackScholes.getVI(tipoOpcao, valorFechamentoAcao.doubleValue(), precoExercicioOpcao);
			
			//atualiza o valor intrinseco
			this.setValorIntrinseco(String.valueOf(vi));
			
			//calcula o VE
			double ve = BlackScholes.getVE(tipoOpcao, valorFechamentoAcao.doubleValue(), precoExercicioOpcao, valorOpcao);
			
			//atualiza o valor extrinseco
			this.setValorExtrinseco(String.valueOf(ve));
			
			//calcula a quantidade de dias para o vencimento
			int diasParaVencimento = SATAUtil.getDiferencaDias(diaPeriodo, diaVencimento);
			
			//recupera a taxa de juros
			BigDecimal taxaJuros = new BigDecimal(SATAUtil.getTaxaDeJuros());
			
			//recupera a volatilidade utilizada
			BigDecimal volatilidadeUtilizada = PrecoOpcao.calculaVolatilidade(tipoOpcao, valorFechamentoAcao, 
											this.getValorPrecoExercicio(), diasParaVencimento, this.getValorFechamento(), taxaJuros);
			
			//atualiza a volatilidade implicita
			this.volatilidadeImplicita = volatilidadeUtilizada.doubleValue();
			
		} catch (Exception e) {
			//nao conseguiu recuperar a cotacao da acao no periodo da opcao
			System.out.println(e);
		}
		
	}
	
}
