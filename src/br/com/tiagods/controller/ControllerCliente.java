package br.com.tiagods.controller;

import br.com.tiagods.model.ComboBoxAutoComplete;
import br.com.tiagods.model.*;
import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Tiago on 13/07/2017.
 */
public class ControllerCliente implements Initializable {
    @FXML
    private JFXTabPane tabPane;

    @FXML
    private Tab tabPesquisa;

    @FXML
    private JFXComboBox<String> cbSituacao;

    @FXML
    private JFXComboBox<Setor> cbSetorPesquisa;

    @FXML
    private JFXTextField txPesquisa;

    @FXML
    private JFXComboBox<String> cbFiltro;

    @FXML
    private TableView<Cliente> tbClientes;

    @FXML
    private PieChart grafico;

    @FXML
    private JFXButton btnNovo1;

    @FXML
    private Tab tabCadastro;

    @FXML
    private AnchorPane pnCadastro;

    @FXML
    private JFXTextField txCodigo;

    @FXML
    private JFXTextField txNome;

    @FXML
    private JFXTextField txEmail;

    @FXML
    private JFXTextField txEndereco;

    @FXML
    private JFXComboBox<Setor> cbSetor;

    @FXML
    private JFXCheckBox ckDesabilitar;

    @FXML
    private JFXButton btnNovo;

    @FXML
    private JFXButton btnAlterar;

    @FXML
    private JFXButton btnSalvar;

    @FXML
    private JFXButton btExcluir;
    
    Cliente cliente;
    ClienteDAO dao = new ClienteDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabela();
        combos();
        graficos();
        List<Cliente> lista = dao.listar(" order by c.nome");
        tbClientes.setItems(FXCollections.observableList(lista));
    }
    private void combos(){
        cbSituacao.getItems().addAll("Ativo","Inativo","Todas");
        cbSituacao.getSelectionModel().select("Todas");
        cbFiltro.getItems().addAll("ID","Nome");
        cbFiltro.getSelectionModel().select("Nome");
        List<Setor> setors = new SetorDAO().listar();
        cbSetor.getItems().add(null);
        if(setors!=null){
            cbSetor.getItems().addAll(setors);
        }
        cbSetorPesquisa.getItems().addAll(cbSetor.getItems());
        new ComboBoxAutoComplete<>(cbSetorPesquisa); //aplicando o autocomplete sobre o combobox
        new ComboBoxAutoComplete<>(cbSetor);

        cbSetorPesquisa.valueProperty().addListener((observable, oldValue, newValue) -> {
            filtrar();
        });
        cbSituacao.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(newValue!=null && !newValue.equals("Situação"))
                filtrar();
        });
        cbFiltro.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue!=null && !newValue.equals("Filtrar por:") && !newValue.equals(""))
                    filtrar();
            }
        });
    }

    private void desbloquear(boolean value) {
        ObservableList<Node> nodes = pnCadastro.getChildren();
        nodes.stream().forEach((n) -> {
            if (n instanceof JFXTextField) {
                ((JFXTextField) n).setEditable(value);
            } else if (n instanceof JFXComboBox) {
                ((JFXComboBox) n).setDisable(!value);
            } else if (n instanceof JFXTextArea) {
                ((JFXTextArea) n).setEditable(value);
            } else if (n instanceof TableView) {
                ((TableView) n).setDisable(!value);
            } else if( n instanceof JFXCheckBox){
                ((JFXCheckBox)n).setDisable(!value);
            }
        });
    }
    @FXML
    private void editar(ActionEvent event){
        if(!txCodigo.getText().equals(""))
            desbloquear(true);
    }
    @FXML
    private void excluir(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exclusão...");
        alert.setHeaderText(null);
        if (!txCodigo.getText().equals("")) {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Tem certeza disso?");
            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.get()==ButtonType.OK){
            try {
                if (dao.excluir(cliente)) {
                    for (Cliente c : tbClientes.getItems()) {
                        if (c.getId() == cliente.getId()) {
                            tbClientes.getItems().remove(c);
                            break;
                        }
                    }
                    tabPane.getSelectionModel().select(tabCadastro);
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setContentText("Registro excluido com sucesso!");
                    alert.showAndWait();
                    limparTela();
                    desbloquear(false);
                    cliente = null;
                }
            } catch (Exception e) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Erro ao excluir o registro:\n "+e.getMessage());
                alert.showAndWait();
            } 
            }
        }
        else{
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Nenhum registro selecionado!");
            alert.showAndWait();
        }
    }
    private void filtrar() {
        String sql="";
        if(cbSituacao.getValue().equals("Ativo")){
            sql+=" and c.ativo=1";
        }
        else if(cbSituacao.getValue().equals("Inativo")){
            sql+=" and c.ativo=0";
        }
        if(cbSetorPesquisa.getValue()!=null){
            sql+=" and c.setor="+cbSetorPesquisa.getValue().getId();
        }
        if(!txPesquisa.getText().trim().equals("")){
            if(cbFiltro.getValue().equals("ID")) {
                sql+=" and c.id="+txPesquisa.getText();
            }
            else {
                sql+=" and c.nome like '"+txPesquisa.getText()+"%'";
            }
        }
        sql+=" order by c.nome";
        List<Cliente> clientes= dao.listar(sql);
        tbClientes.getItems().clear();
        tbClientes.setItems(FXCollections.observableArrayList(clientes));
        graficos();
    }
    private void graficos(){
        Map<String, Integer> map  = dao.agrupar();
        
        if(map!=null){
            ObservableList<PieChart.Data> datas = FXCollections.observableArrayList();
            map.keySet().forEach((s) -> {
                datas.add(new PieChart.Data("Ativo("+map.get(s)+")", map.get(s)));
            });
            grafico.setData(datas);
        }
        grafico.setTitle("Relação de Clientes");
        grafico.setLabelsVisible(true);
        grafico.setLegendVisible(true);
        grafico.setLabelLineLength(10);
    }
    private void limparTela() {
        ObservableList<Node> nodes = pnCadastro.getChildren();
        nodes.stream().forEach((n) -> {
            if (n instanceof JFXTextField) {
                ((JFXTextField) n).setText("");
            } else if (n instanceof JFXComboBox) {
                ((JFXComboBox) n).setValue(null);
            } else if (n instanceof JFXTextArea) {
                ((JFXTextArea) n).setText("");
            } else if (n instanceof TableView) {
                ((TableView) n).getItems().clear();
            } else if(n instanceof  JFXCheckBox){
                ((JFXCheckBox)n).setSelected(false);
            }
        });
    }
    @FXML
    public void novo(ActionEvent event) {
        limparTela();
        desbloquear(true);
        tabPane.getSelectionModel().select(tabCadastro);
        this.cliente = null;
    }
    @FXML
    void novoSetor(ActionEvent event){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Criação de setores");
        dialog.setHeaderText("Crie um novo setor:");
        dialog.setContentText("Por favor entre com um novo nome:");
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            if(result.get().trim().length()==0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Insira um nome para o departamento");
                alert.setContentText(null);
                alert.showAndWait();
            }
            else{
                try {
                    
                    SetorDAO sDao = new SetorDAO();
                    int q = sDao.verificarSetorExiste(result.get().trim());
                    if (q == 0) {
                        Setor setor = new Setor(0,result.get().trim());
                        if (sDao.salvar(setor)) {
                            //preencher combos
                            ObservableList<Setor> novaLista = FXCollections.observableArrayList();
                            novaLista.add(null);
                            novaLista.addAll(novaLista);
                            Setor setor1 = cbSetor.getValue();
                            cbSetor.setItems(novaLista);
                            cbSetorPesquisa.setItems(novaLista);
                            if (setor1 != null)
                                cbSetor.getSelectionModel().select(setor1);

                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Valor duplicado");
                        alert.setContentText("Já existe um cadastro com o texto informado");
                        alert.showAndWait();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    @FXML
    public void pesquisar(KeyEvent event){
        if(!txPesquisa.getText().trim().equals("")){
            filtrar();
        }
    }
    private void preencherFormulario(Cliente cliente){
            txCodigo.setText(String.valueOf(cliente.getId()));
            txNome.setText(cliente.getNome());
            txEmail.setText(cliente.getEmail());
            txEndereco.setText(cliente.getEndereco());
            cbSetor.setValue(cliente.getSetor());
            ckDesabilitar.setSelected(cliente.getAtivo()==0);
            desbloquear(true);
            this.cliente = cliente;

    }
    @FXML
    public void salvar(ActionEvent event){
        if(cbSetor.getValue()!=null){
        dao = new ClienteDAO();
        cliente = new Cliente();
        cliente.setNome(txNome.getText());
        cliente.setEmail(txEmail.getText());
        cliente.setEndereco(txEndereco.getText());
        cliente.setSetor(cbSetor.getValue());
        cliente.setAtivo(ckDesabilitar.isSelected()?0:1);
        try {
            if(txCodigo.getText().equals("")){
                int key = dao.salvar(cliente);
                if(key!=-1){
                    cliente.setId(key);
                    txCodigo.setText(String.valueOf(cliente.getId()));
                    desbloquear(false);
                    tbClientes.getItems().add(cliente);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setContentText("Registro salvo com sucesso!!!");
                    alert.showAndWait();
                    tabPane.getSelectionModel().select(tabPesquisa);
                }
            }
            else{
                cliente.setId(Integer.parseInt(txCodigo.getText()));
                if(dao.atualizar(cliente)){
                    desbloquear(false);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setContentText("Registro salvo com sucesso!!!");
                    alert.showAndWait();
                    tabPane.getSelectionModel().select(tabPesquisa);
                }
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setContentText("Erro ao salvar o registro:\n "+e.getMessage());
            alert.showAndWait();
        }
        filtrar();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setContentText("É obrigatório um Setor para o registro.");
            alert.showAndWait();
        }
    }

    private void tabela(){
        TableColumn<Cliente, Number> colunaId = new TableColumn<>("*");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaId.setPrefWidth(40);
        TableColumn<Cliente, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Cliente, Number> colunaStatus= new TableColumn<>("Status");
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("ativo"));
        colunaStatus.setCellFactory((TableColumn<Cliente, Number> param) -> new TableCell<Cliente, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                    setStyle("");
                } else if(item.intValue()==0){
                    setText("Inativo");
                } else if(item.intValue()==1){
                    setText("Ativo");
                }
            }
        });
        TableColumn<Cliente, Setor> colunaSetor= new TableColumn<>("Setor");
        colunaSetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
        colunaSetor.setCellFactory((TableColumn<Cliente, Setor> param) -> new TableCell<Cliente, Setor>() {
            @Override
            protected void updateItem(Setor item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.getNome());
                } 
            }
        });
        TableColumn<Cliente, String> colunaEditar = new TableColumn<>("");
        colunaEditar.setCellValueFactory(new PropertyValueFactory<>(""));
        colunaEditar.setCellFactory((TableColumn<Cliente, String> param) -> {
            final TableCell<Cliente, String> cell = new TableCell<Cliente, String>() {
                final JFXButton button = new JFXButton("Editar");
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    button.getStyleClass().add("btOrange");
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        button.setOnAction(event -> {
                            tabPane.getSelectionModel().select(tabCadastro);
                            desbloquear(true);
                            limparTela();
                            preencherFormulario(tbClientes.getItems().get(getIndex()));
                        });
                        setGraphic(button);
                        setText(null);
                    }
                }
            };
            return cell;
        });
        tbClientes.getColumns().addAll(colunaId,colunaNome,colunaStatus,colunaSetor,colunaEditar);
    }

}
